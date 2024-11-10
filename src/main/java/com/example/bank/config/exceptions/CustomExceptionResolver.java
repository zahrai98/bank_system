package com.example.bank.config.exceptions;//package com.example.sample.utilities.exceptions;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.web.servlet.HandlerExceptionResolver;
//import org.springframework.web.servlet.ModelAndView;
//
//public class CustomExceptionResolver implements HandlerExceptionResolver {
//
//    @Override
//    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
//        if (ex instanceof NullPointerException) {
//            ModelAndView modelAndView = new ModelAndView("errorPage");
//            modelAndView.addObject("message", "NullPointerException occurred");
//            modelAndView.setViewName("errorPage");
//            return modelAndView;
//        }
//        if (ex instanceof DataNotFoundException) {
//            ModelAndView modelAndView = new ModelAndView("errorPage");
//            modelAndView.addObject("message", "DataNotFoundException occurred");
//            modelAndView.setViewName("errorPage");
//            return modelAndView;
//        }
//
//        return null;
//    }
//}
