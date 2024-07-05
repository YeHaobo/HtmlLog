package com.yhb.hlog.config;

/**日志图片属性*/
public enum ImgAttr {

    /**保持图片宽高不变*/
    NONE("none"),

    /**保持纵横比缩放图片，使图片的长边能完全显示出来*/
    CONTAIN("contain"),

    /**保持纵横比缩放图片，使图片的短边能完全显示出来，长边可能展示不完全*/
    COVER("cover"),

    /**不保持纵横比缩放图片，使图片完全适应*/
    FILL("fill"),

    /**当图片实际宽高小于所设置的图片宽高时，显示效果与none一致；否则显示效果与contain一致*/
    SCALE_DOWN("scale-down");

    /**xml属性*/
    private String xmlAttr;

    /**构造*/
    ImgAttr(String xmlAttr) {
        this.xmlAttr = xmlAttr;
    }

    /**xml属性*/
    private String xmlAttr(){
        return xmlAttr;
    }

}