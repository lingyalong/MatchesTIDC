package com.tidc.match.ov;

import com.tidc.match.pojo.Student;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Description:
 *  用户的令牌，用于鉴权和认证
 * @author 张朝锋
 * @date 2018-09-18
 */
@Data
@ApiModel(description = "用户的令牌，用于鉴权和认证")
public class UserTokenDTO implements Serializable {

    private static final long serialVersionUID = 5802485028976042797L;

    @ApiModelProperty("令牌")
    private String access_token;

    @ApiModelProperty("密码的模式")
    private String token_type;

    @ApiModelProperty("用于刷新的令牌")
    private String refresh_token;

    @ApiModelProperty("token持续时间")
    private Integer expires_in;

    @ApiModelProperty("范围")
    private String scope;
    private Student student;
}
