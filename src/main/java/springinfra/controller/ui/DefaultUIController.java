package springinfra.controller.ui;

import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

public abstract class DefaultUIController implements UIController {

    private ThreadLocal<MultiValueMap<String, String>> requestParamsHolder = new ThreadLocal<>();

    public String render(Map<String, Object> model, @RequestParam MultiValueMap<String, String> requestParams) {
        this.requestParamsHolder.set(requestParams);
        try {
            return this.render(model);
        } finally {
            this.requestParamsHolder.remove();
        }
    }

    public abstract String render(Map<String, Object> model);

    protected MultiValueMap<String, String> getRequestParams() {
        return this.requestParamsHolder.get();
    }
}
