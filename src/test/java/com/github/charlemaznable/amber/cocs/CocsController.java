package com.github.charlemaznable.amber.cocs;

import com.github.charlemaznable.amber.AmberLogin;
import com.github.charlemaznable.amber.spring.AmberCocsHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
