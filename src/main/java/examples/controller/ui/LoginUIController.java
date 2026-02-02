package examples.controller.ui;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springinfra.configuration.security.idp.BaseIdentityProviderModuleConfig;
import org.springinfra.configuration.security.idp.BuiltInIdentityProviderConfig;
import org.springinfra.configuration.security.idp.OidcIdentityProviderModuleConfig;
import org.springinfra.controller.ui.DefaultUIController;
import org.springinfra.utility.identity.IdentityUtil;

import java.text.MessageFormat;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping(value = {"/login"})
@RequiredArgsConstructor
public class LoginUIController extends DefaultUIController {

    public static final String BUILT_IN_LOGIN_JSP = "builtInLogin.jsp";
    public static final String OIDC_LOGIN_JSP = "oidcLogin.jsp";

    private final Optional<BaseIdentityProviderModuleConfig> identityProviderModuleConfig;

    @Override
    public String render(Map<String, Object> model) {
        if (IdentityUtil.isAuthenticated()) {
            return "redirect:/";
        }

        if (this.identityProviderModuleConfig.isEmpty()) {
            // We don't support any authentication mechanism
            return "redirect:/";
        }

        if (this.identityProviderModuleConfig.get() instanceof BuiltInIdentityProviderConfig) {
            return BUILT_IN_LOGIN_JSP;
        }

        if (this.identityProviderModuleConfig.get() instanceof OidcIdentityProviderModuleConfig oidcIdentityProviderModuleConfig) {
            Map<String, String> oidcLoginLinks = oidcIdentityProviderModuleConfig.getLoginLinks();
            if (oidcLoginLinks.size() == 1) {   // If we have only one OIDC client (that usually does), we redirect the user to it directly instead of going to the custom OIDC login page
                return "redirect:" + oidcLoginLinks.keySet().stream().findFirst().orElseThrow();
            }
            model.put("oidcLoginLinks", oidcLoginLinks);
            return OIDC_LOGIN_JSP;
        }

        throw new AssertionError(MessageFormat.format("Not proper logic is implemented for identity module {0}", identityProviderModuleConfig.get().getClass().getName()));
    }
}
