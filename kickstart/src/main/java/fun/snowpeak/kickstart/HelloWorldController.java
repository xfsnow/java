package fun.snowpeak.kickstart;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import javax.servlet.http.HttpServletRequest;

@EnableAutoConfiguration
@Controller
public class HelloWorldController {

    @RequestMapping("/")
    @ResponseBody
    public String index(HttpServletRequest request) {
        Date date = new Date();
        String strDateFormat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
        // 设置时区为东 8 区
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        String dateTimeStr = sdf.format(date);

        String clientIp = IpUtil.getIpAddress(request);
        StringBuilder sb = new StringBuilder();
        sb.append("<h1>Hello Spring Boot</h1><p> Current time is ")
                .append(dateTimeStr).append(".</p><p> Your IP is ")
                .append(clientIp)
                .append(".</p><p>This is a demo project of Spring Boot package in Jar artifact.</p><p><a href=\"/hello\">hello</a></p>");
        return sb.toString();
    }

    @RequestMapping("/hello")
    @ResponseBody
    public String hello() {
        return "Hello World 世界你好！<p><a href=\"/\">Back to homepage.</a></p>";
    }
}