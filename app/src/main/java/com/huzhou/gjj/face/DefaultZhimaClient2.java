//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.huzhou.gjj.face;

import com.antgroup.zmxy.openplatform.api.ZhimaApiException;
import com.antgroup.zmxy.openplatform.api.ZhimaClient;
import com.antgroup.zmxy.openplatform.api.ZhimaParser;
import com.antgroup.zmxy.openplatform.api.ZhimaRequest;
import com.antgroup.zmxy.openplatform.api.ZhimaResponse;
import com.antgroup.zmxy.openplatform.api.ZhimaUploadRequest;
import com.antgroup.zmxy.openplatform.api.internal.parser.json.ObjectJsonParser;
import com.antgroup.zmxy.openplatform.api.internal.util.AESUtil;
import com.antgroup.zmxy.openplatform.api.internal.util.EncryptionModeEnum;
import com.antgroup.zmxy.openplatform.api.internal.util.RSACoderUtil;
import com.antgroup.zmxy.openplatform.api.internal.util.SignTypeEnum;
import com.antgroup.zmxy.openplatform.api.internal.util.StringUtils;
import com.antgroup.zmxy.openplatform.api.internal.util.TimeoutConfig;
import com.antgroup.zmxy.openplatform.api.internal.util.ZhimaHashMap;
import com.antgroup.zmxy.openplatform.api.internal.util.ZhimaLogger;
import com.antgroup.zmxy.openplatform.api.internal.util.ZhimaUtils;
import com.antgroup.zmxy.openplatform.api.request.ZhimaCustomerCertificationInitializeRequest;
import com.antgroup.zmxy.openplatform.api.request.ZhimaCustomerCertificationQueryRequest;

import java.io.File;
import java.net.URLEncoder;
import java.security.Security;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class DefaultZhimaClient2 implements ZhimaClient {
    private String gatewayUrl;
    private String appId;
    private EncryptionModeEnum encryptionMode;
    private SignTypeEnum signType;
    private String aesKey;
    private String privateKey;
    private String zhimaPublicKey;
    private String bizParam;
    private String charset;
    private int connectTimeout;
    private int readTimeout;
    private int uploadTimeout;

    public DefaultZhimaClient2(String gatewayUrl, String appId, String privateKey, String zhimaPublicKey) {
        this.signType = SignTypeEnum.SHA1WITHRSA;
        this.bizParam = "params";
        this.charset = "UTF-8";
        this.connectTimeout = 6000;
        this.readTimeout = '\uea60';
        this.uploadTimeout = 7200000;
        this.gatewayUrl = gatewayUrl;
        this.appId = appId;
        this.privateKey = privateKey;
        this.zhimaPublicKey = zhimaPublicKey;
    }

    public DefaultZhimaClient2(String gatewayUrl, String appId, String charset, String privateKey, String zhimaPublicKey) {
        this.signType = SignTypeEnum.SHA1WITHRSA;
        this.bizParam = "params";
        this.charset = "UTF-8";
        this.connectTimeout = 6000;
        this.readTimeout = '\uea60';
        this.uploadTimeout = 7200000;
        this.gatewayUrl = gatewayUrl;
        this.appId = appId;
        this.privateKey = privateKey;
        this.charset = charset;
        this.zhimaPublicKey = zhimaPublicKey;
    }

    public DefaultZhimaClient2(String gatewayUrl, String appId, String charset, String privateKey, String zhimaPublicKey, TimeoutConfig timeoutConfig) {
        this.signType = SignTypeEnum.SHA1WITHRSA;
        this.bizParam = "params";
        this.charset = "UTF-8";
        this.connectTimeout = 6000;
        this.readTimeout = '\uea60';
        this.uploadTimeout = 7200000;
        this.gatewayUrl = gatewayUrl;
        this.appId = appId;
        this.privateKey = privateKey;
        this.charset = charset;
        this.zhimaPublicKey = zhimaPublicKey;
        this.initTimeoutConfig(timeoutConfig);
    }

    public DefaultZhimaClient2(String gatewayUrl, String appId, String charset, String privateKey, String zhimaPublicKey, EncryptionModeEnum encrytionMode) {
        this.signType = SignTypeEnum.SHA1WITHRSA;
        this.bizParam = "params";
        this.charset = "UTF-8";
        this.connectTimeout = 6000;
        this.readTimeout = '\uea60';
        this.uploadTimeout = 7200000;
        this.gatewayUrl = gatewayUrl;
        this.appId = appId;
        this.privateKey = privateKey;
        this.charset = charset;
        this.zhimaPublicKey = zhimaPublicKey;
        this.encryptionMode = encrytionMode;
    }

    public DefaultZhimaClient2(String gatewayUrl, String appId, String charset, String privateKey, String zhimaPublicKey, EncryptionModeEnum encrytionMode, SignTypeEnum signType, String aesKey) {
        this.signType = SignTypeEnum.SHA1WITHRSA;
        this.bizParam = "params";
        this.charset = "UTF-8";
        this.connectTimeout = 6000;
        this.readTimeout = '\uea60';
        this.uploadTimeout = 7200000;
        this.gatewayUrl = gatewayUrl;
        this.appId = appId;
        this.privateKey = privateKey;
        this.charset = charset;
        this.zhimaPublicKey = zhimaPublicKey;
        this.encryptionMode = encrytionMode;
        this.signType = signType;
        this.aesKey = aesKey;
    }

    public <T extends ZhimaResponse> T execute(ZhimaRequest<T> request) throws ZhimaApiException {
        ObjectJsonParser<T> parser = null;
        parser = new ObjectJsonParser<>(request.getResponseClass());
        return this._execute(request, parser);
    }

    public String generatePageRedirectInvokeUrl(ZhimaRequest request) throws ZhimaApiException {
        try {
            String e = WebUtils2.buildQuery(this.getSystemParams(request), this.charset);
            StringBuffer urlSb = new StringBuffer(this.gatewayUrl);
            urlSb.append("?");
            urlSb.append(e);
            String encryptedAppParam = this.generateEncryptedParam(request);
            urlSb.append("&").append(this.bizParam).append("=").append(URLEncoder.encode(encryptedAppParam, this.charset));
            return urlSb.toString();
        } catch (Exception var5) {
            throw new ZhimaApiException(var5);
        }
    }

    public String decryptAndVerifySign(String encryptedResponse, String sign) throws ZhimaApiException {
        try {
            String e = RSACoderUtil.decrypt(encryptedResponse, this.privateKey, this.charset, this.encryptionMode);
            boolean verifyResult = RSACoderUtil.verify(e.getBytes(this.charset), this.zhimaPublicKey, sign);
            if (!verifyResult) {
                throw new ZhimaApiException("验签失败");
            } else {
                return e;
            }
        } catch (Exception var5) {
            throw new ZhimaApiException(var5);
        }
    }

    private ZhimaHashMap getSystemParams(ZhimaRequest request) throws ZhimaApiException {
        try {
            ZhimaHashMap e = new ZhimaHashMap();
            e.put("method", request.getApiMethodName());
            e.put("version", request.getApiVersion());
            e.put("app_id", this.appId);
            e.put("charset", this.charset);
            e.put("scene", request.getScene());
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            df.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            e.put("channel", request.getChannel());
            e.put("platform", request.getPlatform());
            e.put("ext_params", request.getExtParams());
            String bizParamStr = this.getBizParamStr(request);
            String signContent = RSACoderUtil.sign(this.signType, bizParamStr, this.charset, this.privateKey);
            e.put("sign", signContent);
            e.put("sign_type", this.signType.getCode());
            if (this.encryptionMode == EncryptionModeEnum.AES) {
                String encryptedAesKey = RSACoderUtil.encrypt(this.aesKey, this.charset, this.zhimaPublicKey);
                e.put("encrypted_key", encryptedAesKey);
                e.put("encryption_mode", EncryptionModeEnum.AES.getCode());
            }

            return e;
        } catch (Exception var7) {
            throw new ZhimaApiException(var7);
        }
    }

    public String generateSign(ZhimaRequest request) throws ZhimaApiException {
        try {
            String e = this.getBizParamStr(request);
            String signContent = RSACoderUtil.sign(e, this.charset, this.privateKey);
            return signContent;
        } catch (Exception var4) {
            throw new ZhimaApiException(var4);
        }
    }

    public String generateSignWithUrlEncode(ZhimaRequest request) throws ZhimaApiException {
        try {
            String e = this.generateSign(request);
            return URLEncoder.encode(e, this.charset);
        } catch (Exception var3) {
            throw new ZhimaApiException(var3);
        }
    }

    public String generateEncryptedParam(ZhimaRequest request) throws ZhimaApiException {
        try {
            String e = this.getBizParamStr(request);
            String encryptedBizParam = RSACoderUtil.encrypt(e, this.charset, this.zhimaPublicKey, this.encryptionMode);
            return encryptedBizParam;
        } catch (Exception var4) {
            throw new ZhimaApiException(var4);
        }
    }

    public String generateEncryptedParamWithUrlEncode(ZhimaRequest request) throws ZhimaApiException {
        try {
            String e = this.generateEncryptedParam(request);
            return URLEncoder.encode(e, this.charset);
        } catch (Exception var3) {
            throw new ZhimaApiException(var3);
        }
    }

    private String getBizParamStr(ZhimaRequest request) throws ZhimaApiException {
        try {
            Map<String, String> e = request.getTextParams();
            String bizParamStr = WebUtils2.buildQuery(e, this.charset);
            return bizParamStr;
        } catch (Exception var4) {
            throw new ZhimaApiException(var4);
        }
    }

    private <T extends ZhimaResponse> T _execute(ZhimaRequest<T> request, ZhimaParser<T> parser) throws ZhimaApiException {
        Map rt = this.doPost(request);
        if (rt == null) {
            return null;
        } else {
            ZhimaResponse tRsp = null;

            try {
                tRsp = parser.parse((String) rt.get("rsp"));
                tRsp.setBody((String) rt.get("rsp"));
            } catch (RuntimeException var6) {
                ZhimaLogger.logBizError((String) rt.get("rsp"));
                throw var6;
            } catch (ZhimaApiException var7) {
                ZhimaLogger.logBizError((String) rt.get("rsp"));
                throw new ZhimaApiException(var7);
            }

            tRsp.setParams((ZhimaHashMap) rt.get("textParams"));
            if (!tRsp.isSuccess()) {
                ZhimaLogger.logErrorScene(rt, tRsp, "");
            }

            return (T) tRsp;
        }
    }

    public <T extends ZhimaResponse> Map<String, Object> doPost(ZhimaRequest<T> request) throws ZhimaApiException {
        try {
            HashMap<String, Object> e = new HashMap<>();
            String bizParamStr = this.getBizParamStr(request);
            String encryptedBizParam = null;
            if (this.encryptionMode == EncryptionModeEnum.AES) {
                encryptedBizParam = AESUtil.encrypt(bizParamStr, this.aesKey, this.charset);
            } else {
                encryptedBizParam = RSACoderUtil.encrypt(bizParamStr, this.charset, this.zhimaPublicKey, this.encryptionMode);
            }

            HashMap<String, String> encodedBizParamMap = new HashMap<>();
            encodedBizParamMap.put(this.bizParam, encryptedBizParam);
            ZhimaHashMap appParams = new ZhimaHashMap(encodedBizParamMap);
            if (StringUtils.isEmpty(this.charset)) {
                this.charset = "UTF-8";
            }

            ZhimaHashMap sysParams = this.getSystemParams(request);
            String sysQueryParam = WebUtils2.buildQuery(sysParams, this.charset);
            StringBuffer urlSb = new StringBuffer(this.gatewayUrl);
            urlSb.append("?");
            urlSb.append(sysQueryParam);
            String fullRsp = null;
            ZhimaUploadRequest<ZhimaResponse> decryptedRsp;
            if (request instanceof ZhimaUploadRequest) {
                decryptedRsp = (ZhimaUploadRequest<ZhimaResponse>) request;
                Map<String, com.antgroup.zmxy.openplatform.api.FileItem> fileParams = ZhimaUtils.cleanupMap(decryptedRsp.getFileParams());
                fullRsp = WebUtils2.doPost(urlSb.toString(), appParams, fileParams, this.charset, this.connectTimeout, this.uploadTimeout);
            } else {
                fullRsp = WebUtils2.doPost(urlSb.toString(), appParams, this.charset, this.connectTimeout, this.readTimeout);
            }

            decryptedRsp = null;
            String decryptedRsp1;
            if (this.encryptionMode == EncryptionModeEnum.AES) {
                decryptedRsp1 = AESUtil.decryptResponse(fullRsp, this.aesKey, this.charset);
            } else {
                decryptedRsp1 = RSACoderUtil.decryptResponse(fullRsp, this.privateKey, this.charset, this.encryptionMode);
            }

            RSACoderUtil.verifySign(this.signType, fullRsp, decryptedRsp1, this.zhimaPublicKey, this.charset);
            e.put("rsp", decryptedRsp1);
            e.put("textParams", appParams);
            e.put("protocalParams", sysParams);
            e.put("url", urlSb.toString());
            return e;
        } catch (Exception var13) {
            throw new ZhimaApiException(var13);
        }
    }

    public void encryptFileWithAES(String sourceFile, String targetFile) throws Exception {
        AESUtil.encryptFile(new File(sourceFile), new File(targetFile), this.aesKey, targetFile);
    }

    private void initTimeoutConfig(TimeoutConfig timeoutConfig) {
        if (timeoutConfig != null) {
            if (timeoutConfig.getConnectTimeout() > 0) {
                this.connectTimeout = timeoutConfig.getConnectTimeout();
            }

            if (timeoutConfig.getRequestTimeout() > 0) {
                this.readTimeout = timeoutConfig.getRequestTimeout();
            }

            if (timeoutConfig.getFileUploadTimeout() > 0) {
                this.uploadTimeout = timeoutConfig.getFileUploadTimeout();
            }
        }

    }

//     新增加  获取 biz_no

    public String execute2(ZhimaCustomerCertificationInitializeRequest request) throws ZhimaApiException {
        try {
            String bizParamStr = this.getBizParamStr(request);
            String encryptedBizParam;
            if (this.encryptionMode == EncryptionModeEnum.AES) {
                encryptedBizParam = AESUtil.encrypt(bizParamStr, this.aesKey, this.charset);
            } else {
                encryptedBizParam = RSACoderUtil.encrypt(bizParamStr, this.charset, this.zhimaPublicKey, this.encryptionMode);
            }
            HashMap encodedBizParamMap = new HashMap();
            encodedBizParamMap.put(this.bizParam, encryptedBizParam);
            ZhimaHashMap appParams = new ZhimaHashMap(encodedBizParamMap);
            if (StringUtils.isEmpty(this.charset)) {
                this.charset = "UTF-8";
            }
            ZhimaHashMap sysParams = this.getSystemParams(request);
            String sysQueryParam = WebUtils2.buildQuery(sysParams, this.charset);
            StringBuffer urlSb = new StringBuffer(this.gatewayUrl);
            urlSb.append("?");
            urlSb.append(sysQueryParam);
            String fullRsp;
            ZhimaUploadRequest decryptedRsp;
            if (request instanceof ZhimaUploadRequest) {
                decryptedRsp = (ZhimaUploadRequest) request;
                Map fileParams = ZhimaUtils.cleanupMap(decryptedRsp.getFileParams());
                fullRsp = WebUtils2.doPost(urlSb.toString(), appParams, fileParams, this.charset, this.connectTimeout, this.uploadTimeout);
            } else {
                fullRsp = WebUtils2.doPost(urlSb.toString(), appParams, this.charset, this.connectTimeout, this.readTimeout);
            }
            String decryptedRsp1;
            if (this.encryptionMode == EncryptionModeEnum.AES) {
                decryptedRsp1 = AESUtil.decryptResponse(fullRsp, this.aesKey, this.charset);
            } else {
                decryptedRsp1 = RSACoderUtil.decryptResponse(fullRsp, this.privateKey, this.charset, this.encryptionMode);
            }

            RSACoderUtil.verifySign(this.signType, fullRsp, decryptedRsp1, this.zhimaPublicKey, this.charset);
            return decryptedRsp1;
        } catch (Exception var13) {
            throw new ZhimaApiException(var13);
        }
    }


    //     查询
    public String execute3(ZhimaCustomerCertificationQueryRequest request) throws ZhimaApiException {
        try {
            String bizParamStr = this.getBizParamStr(request);
            String encryptedBizParam;
            if (this.encryptionMode == EncryptionModeEnum.AES) {
                encryptedBizParam = AESUtil.encrypt(bizParamStr, this.aesKey, this.charset);
            } else {
                encryptedBizParam = RSACoderUtil.encrypt(bizParamStr, this.charset, this.zhimaPublicKey, this.encryptionMode);
            }

            HashMap encodedBizParamMap = new HashMap();
            encodedBizParamMap.put(this.bizParam, encryptedBizParam);
            ZhimaHashMap appParams = new ZhimaHashMap(encodedBizParamMap);
            if (StringUtils.isEmpty(this.charset)) {
                this.charset = "UTF-8";
            }

            ZhimaHashMap sysParams = this.getSystemParams(request);
            String sysQueryParam = WebUtils2.buildQuery(sysParams, this.charset);
            StringBuffer urlSb = new StringBuffer(this.gatewayUrl);
            urlSb.append("?");
            urlSb.append(sysQueryParam);
            String fullRsp;
            ZhimaUploadRequest decryptedRsp;
            if (request instanceof ZhimaUploadRequest) {
                decryptedRsp = (ZhimaUploadRequest) request;
                Map fileParams = ZhimaUtils.cleanupMap(decryptedRsp.getFileParams());
                fullRsp = WebUtils2.doPost(urlSb.toString(), appParams, fileParams, this.charset, this.connectTimeout, this.uploadTimeout);
            } else {
                fullRsp = WebUtils2.doPost(urlSb.toString(), appParams, this.charset, this.connectTimeout, this.readTimeout);
            }

            String decryptedRsp1;
            if (this.encryptionMode == EncryptionModeEnum.AES) {
                decryptedRsp1 = AESUtil.decryptResponse(fullRsp, this.aesKey, this.charset);
            } else {
                decryptedRsp1 = RSACoderUtil.decryptResponse(fullRsp, this.privateKey, this.charset, this.encryptionMode);
            }

            RSACoderUtil.verifySign(this.signType, fullRsp, decryptedRsp1, this.zhimaPublicKey, this.charset);
            return decryptedRsp1;
        } catch (Exception var13) {
            throw new ZhimaApiException(var13);
        }
    }


    static {
        Security.setProperty("jdk.certpath.disabledAlgorithms", "");
    }
}
