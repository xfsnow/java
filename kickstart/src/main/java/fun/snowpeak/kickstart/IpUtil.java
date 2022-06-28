package fun.snowpeak.kickstart;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;

public class IpUtil {
    /***
    * 获取客户端ip地址
    * @param request
    */
   public static String getIpAddress(HttpServletRequest request) {
       String Xip = request.getHeader("X-Real-IP");
       String XFor = request.getHeader("X-Forwarded-For");

       if (StringUtils.hasLength(XFor) && !"unKnown".equalsIgnoreCase(XFor)) {
        //多次反向代理后会有多个ip值，第一个ip才是真实ip
        int index = XFor.indexOf(",");
        if (index != -1) {
        return XFor.substring(0, index);
        } else {
        return XFor;
        }
       }
       XFor = Xip;
       if (StringUtils.hasLength(XFor) && !"unKnown".equalsIgnoreCase(XFor)) {
        return XFor;
       }
       if (!StringUtils.hasLength(XFor) || "unknown".equalsIgnoreCase(XFor)) {
        XFor = request.getHeader("Proxy-Client-IP");
       }
       if (!StringUtils.hasLength(XFor) || "unknown".equalsIgnoreCase(XFor)) {
        XFor = request.getHeader("WL-Proxy-Client-IP");
       }
       if (!StringUtils.hasLength(XFor) || "unknown".equalsIgnoreCase(XFor)) {
        XFor = request.getHeader("HTTP_CLIENT_IP");
       }
       if (!StringUtils.hasLength(XFor) || "unknown".equalsIgnoreCase(XFor)) {
        XFor = request.getHeader("HTTP_X_FORWARDED_FOR");
       }
       if (!StringUtils.hasLength(XFor) || "unknown".equalsIgnoreCase(XFor)) {
        XFor = request.getRemoteAddr();
       }
       return XFor;
   }
}
