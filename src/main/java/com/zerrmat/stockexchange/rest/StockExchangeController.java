package com.zerrmat.stockexchange.rest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class StockExchangeController {
    @RequestMapping(value = "/")
    public String index() {
        return "index";
    }
}
