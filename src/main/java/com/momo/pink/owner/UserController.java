package com.momo.pink.owner;

import com.momo.pink.Owner;
import com.momo.pink.OwnerService;
import com.momo.pink.User;
import com.momo.pink.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@RequestMapping("api/v1.0/users")
public class UserController {

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.POST, path = "")
    @ResponseBody
    public User addUser(@RequestBody User user) {
        Owner owner = ownerService.addOwner(new Owner()
            .setName(user.getName())
            .setType(Owner.USER_TYPE));
        userService.addUser(user.setId(
            owner.getId()));
        return user;
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/{name}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable("name") String name, HttpServletResponse response) {
        User user = userService.getUser(name);
        if (user == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        userService.deleteUser(name);
        ownerService.deleteOwner(user.getId());
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{name}")
    @ResponseBody
    public User getUser(@PathVariable("name") String name, HttpServletResponse response) {
        User user = userService.getUser(name);
        if (user == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
        return user;
    }

    @RequestMapping(method = RequestMethod.GET, path = "")
    @ResponseBody
    public List<User> listUsers() {
        return userService.listUsers();
    }

    @RequestMapping(method = RequestMethod.GET, path = "/self")
    @ResponseBody
    public User self(OAuth2Authentication principal) {
        User user = userService.getUser(principal.getName());
        if (user == null) {
            Authentication userAuthentication = principal.getUserAuthentication();
            @SuppressWarnings("unchecked")
            Map<String, String> details = (Map<String, String>) userAuthentication.getDetails();
            user = addUser(new User().setName(principal.getName())
                .setEmail(details.get("email")));
        }
        return user;
    }

}
