package com.fosung.ksh.punsh.support;

import org.springframework.core.Ordered;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MutiCharacterEncodingFilter extends CharacterEncodingFilter implements Ordered {
    //最高优先级
    private int order = Integer.MIN_VALUE;


    private List<String> mutiUrls = new ArrayList();

    private String mutiCharset = "GB2312";


    public List<String> getMutiUrls() {
        return mutiUrls;
    }

    public void setMutiUrls(List<String> mutiUrls) {
        this.mutiUrls = mutiUrls;
    }

    public String getMutiCharset() {
        return mutiCharset;
    }

    public void setMutiCharset(String mutiCharset) {
        this.mutiCharset = mutiCharset;
    }


    /**
     * @param charset
     * @param mutiCharset
     * @param mutiUrls
     * @param forceRequest
     * @param forceResponse
     */
    public MutiCharacterEncodingFilter(String charset, String mutiCharset, List<String> mutiUrls, boolean forceRequest, boolean forceResponse) {
        super(charset, forceRequest, forceResponse);
        this.mutiUrls = mutiUrls;
        this.mutiCharset = mutiCharset;

    }

    public MutiCharacterEncodingFilter() {
        super();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        //如果是这个链接 执行mutyEncoding方法
        if (matchAny(mutiUrls, path)) {
            if (mutiCharset != null) {
                if (isForceRequestEncoding() || request.getCharacterEncoding() == null) {
                    request.setCharacterEncoding(mutiCharset);
                }
                if (isForceResponseEncoding()) {
                    response.setCharacterEncoding(mutiCharset);
                }
            }
            filterChain.doFilter(request, response);
            return;
        }

        //否则 使用默认方法
        super.doFilterInternal(request, response, filterChain);
    }

    private boolean matchAny(List<String> mutiUrls, String path) {
        if (mutiUrls == null || mutiUrls.isEmpty()) {
            return false;
        }
        Optional<String> firstUrl = mutiUrls.stream().filter(s -> path.indexOf(s) > -1).findFirst();
        if (firstUrl.isPresent()) {
            return true;
        }
        return false;
    }


    @Override
    public int getOrder() {
        return order;
    }

}
