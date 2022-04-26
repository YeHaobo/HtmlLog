package com.yhb.hlog.config.type;

/**图片展示类型*/
public interface ImgType {

    /**保持图片宽高不变*/
    String NONE = "none";

    /**保持纵横比缩放图片,使图片的长边能完全显示出来*/
    String CONTAIN = "contain";

    /**不保持纵横比缩放图片,使图片完全适应*/
    String FILL = "fill";

    /**保持纵横比缩放图片,只保证图片的短边能完全显示出来*/
    String COVER = "cover";

    /**当图片实际宽高小于所设置的图片宽高时,显示效果与none一致;否则,显示效果与contain一致*/
    String SCALE_DOWN = "scale-down";

}