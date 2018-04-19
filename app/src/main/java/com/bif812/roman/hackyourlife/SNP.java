package com.bif812.roman.hackyourlife;

import java.io.Serializable;


public class SNP implements Serializable {
    private String id;
    private String code;
    private String nuc;
    private String exp;


    public SNP (String id, String code, String nuc, String exp){
        this.id = id;
        this.code = code;
        this.nuc = nuc;
        this.exp = exp;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String name) {
        this.code = code;
    }
    public String getnuc() {
        return nuc;
    }
    public void setnuc (String nuc) {  this.nuc = nuc;}
    public String getexp() {
        return exp;
    }
    public void setexp (String exp) {  this.exp = exp;}
}
