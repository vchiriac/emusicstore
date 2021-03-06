package com.emusicstore.controller;

import com.emusicstore.enums.RoleEnum;
import com.emusicstore.model.*;
import com.emusicstore.service.CartItemService;
import com.emusicstore.service.CartService;
import com.emusicstore.service.CustomerService;
import com.emusicstore.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping("/rest/cart")
@SessionAttributes("roles")
public class CartResources {

    @Autowired
    private CartService cartService;

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ProductService productService;


    @Autowired
    AuthenticationTrustResolver authenticationTrustResolver;

    @RequestMapping("/{cartId}")
    public
    @ResponseBody
    Cart getCartById(@PathVariable(value = "cartId") int cartId) {
        return cartService.getCartById(cartId);
    }

    @RequestMapping(value = "/add/{productId}", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void addItem(@PathVariable(value = "productId") int productId, @AuthenticationPrincipal User activeUser) {
        Customer customer = null;

        final String sessionID = RequestContextHolder.currentRequestAttributes().getSessionId();

        if(isCurrentAuthenticationAnonymous()) {

            final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String temporaryUser = (String) authentication.getPrincipal();
            temporaryUser = temporaryUser + sessionID;

            customer = customerService.getCustomerByUsername(temporaryUser);
            if(customer == null) {

                Collection<? extends GrantedAuthority> grantedAuthorities = authentication.getAuthorities();
                String role = null;

                for (GrantedAuthority grantedAuthority : grantedAuthorities) {
                    role = grantedAuthority.getAuthority();
                }

                RoleEnum roleEnum = RoleEnum.valueOf(role);
                Customer temporaryCustomer = new Customer();
                temporaryCustomer.setUsername(temporaryUser);
                temporaryCustomer.setEnabled(true);
                temporaryCustomer.setPassword(temporaryUser);
                temporaryCustomer.setCustomerName(temporaryUser);
                temporaryCustomer.setCustomerEmail("internal_" + sessionID + "@gmail.com");
                BillingAddress billingAddress = new BillingAddress();
                ShippingAddress shippingAddress = new ShippingAddress();
                temporaryCustomer.setBillingAddress(billingAddress);
                temporaryCustomer.setShippingAddress(shippingAddress);
                temporaryCustomer.setCustomerPhone("111111");
                customerService.addCustomer(temporaryCustomer, roleEnum);
                customer = customerService.getCustomerByUsername(temporaryUser);
            }
        } else {
            customer = customerService.getCustomerByUsername(activeUser.getUsername());
        }

        Cart cart = customer.getCart();
        Product product = productService.getProductById(productId);
        List<CartItem> cartItems = cart.getCartItems();

        for (int i = 0; i < cartItems.size(); i++) {
            if (product.getProductId() == cartItems.get(i).getProduct().getProductId()) {
                CartItem cartItem = cartItems.get(i);
                cartItem.setQuantity(cartItem.getQuantity() + 1);
                cartItem.setTotalPrice(product.getProductPrice() * cartItem.getQuantity());
                cartItemService.addCartItem(cartItem);

                return;
            }
        }

        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(1);
        cartItem.setTotalPrice(product.getProductPrice() * cartItem.getQuantity());
        cartItem.setCart(cart);
        cartItemService.addCartItem(cartItem);
    }

    @RequestMapping(value = "/remove/{productId}", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void removeItem(@PathVariable(value = "productId") int productId) {
        CartItem cartItem = cartItemService.getCartItemByProductId(productId);
        cartItemService.removeCartItem(cartItem);

    }

    @RequestMapping(value = "/{cartId}", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void clearCart(@PathVariable(value = "cartId") int cartId) {
        Cart cart = cartService.getCartById(cartId);
        cartItemService.removeAllCartItems(cart);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Illegal request, please verify your payload")
    public void handleClientErrors(Exception ex) {

    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Internal Server Error")
    public void handleServerErrors(Exception ex) {

    }


    /**
     * This method returns true if users is already authenticated [logged-in], else false.
     */
    private boolean isCurrentAuthenticationAnonymous() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authenticationTrustResolver.isAnonymous(authentication);
    }

} // The End of Class;
