package com.moxie.commons;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import com.moxie.commons.model.JwtToken;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangyanbo on 16/11/28.
 */
@Slf4j
public class BaseJwtUtils {
    public static final String ISSUE_AT = "iat";
    public static final String EXPIRE_AT = "exp";
    public static final String CONTENT = "content";

    public static JwtToken getToken(Object content, long expireInSecond, String secret) {
        return getToken(content, new Date(), expireInSecond, secret);
    }

    /**
     * 生成jwt token
     */
    public static JwtToken getToken(Object content, Date validTime, long expireInSecond, String secret) {
        Preconditions.checkArgument(content != null, "加密内容不能为空");
        Preconditions.checkArgument(expireInSecond > 0, "token有效时间必须为正整数,实际值[" + expireInSecond + "]");
        Preconditions.checkArgument(StringUtils.isNotBlank(secret), "jwt密钥不能为空");

        //准备数据
        final long iat = validTime == null ? System.currentTimeMillis() / 1000l : validTime.getTime() / 1000;
        final long exp = iat + expireInSecond;
        final HashMap<String, Object> claims = new HashMap<>();
        claims.put(ISSUE_AT, iat);
        claims.put(EXPIRE_AT, exp);
        claims.put(CONTENT, content);

        //用密钥生成token
        final JWTSigner signer = new JWTSigner(secret);
        final String token = signer.sign(claims);

        JwtToken jwtToken = new JwtToken();
        jwtToken.setToken(token);
        jwtToken.setExpireAt(exp * 1000);
        return jwtToken;
    }

    /**
     * 校验token并返回token中的内容
     */
    public static <T> T verifyToken(String jwtToken, String secret, Class<T> resultCls) {
        jwtToken = BaseStringUtils.substringAfter(jwtToken, " ").trim();
        Preconditions.checkArgument(StringUtils.isNotBlank(jwtToken), "token不能为空");
        Preconditions.checkArgument(StringUtils.isNotBlank(secret), "jwt密钥不能为空");
        Preconditions.checkArgument(resultCls != null, "jwt内容类型不能为空");

        //校验token

        try {
            final JWTVerifier verifier = new JWTVerifier(secret);
            final Map<String, Object> claims = verifier.verify(jwtToken);
            return (T) BaseJsonUtils.readValue((Map) claims.get(CONTENT), resultCls);
        } catch (Throwable e) {
            log.error("token[{}]校验异常: {}", jwtToken, ExceptionUtils.getStackTrace(e));
            return null;
        }
    }

    public static Map<String, Object> getTokenInfo(String jwtToken) {
        jwtToken = BaseStringUtils.substringAfter(jwtToken, " ").trim();
        Preconditions.checkArgument(StringUtils.isNotBlank(jwtToken), "token不能为空");

        try {
            String contentPart = StringUtils.substringBetween(jwtToken, ".", ".");
            return BaseJsonUtils.readValue(BaseSecurityUtils.base64Decode(contentPart), Map.class);
        } catch (Throwable e) {
            log.error("token[{}]格式不正确", jwtToken);
            return null;
        }
    }
}
