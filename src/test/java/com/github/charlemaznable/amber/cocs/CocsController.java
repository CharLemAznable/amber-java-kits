package com.github.charlemaznable.amber.cocs;

import com.github.charlemaznable.amber.AmberLogin;
import com.github.charlemaznable.amber.spring.AmberCocsHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@AmberLogin(required = false)
@Controller
public class CocsController {

    @Autowired
    private AmberCocsHandler cocsHandler;

    @RequestMapping("/cocs")
    public void cocs(HttpServletRequest request, HttpServletResponse response) {
        cocsHandler.handle(request, response);
    }
}
