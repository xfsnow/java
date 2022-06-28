package fun.snowpeak.spring;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@RestController
public class HelloController {

    @GetMapping("/")
	public String index(HttpServletRequest request) {
        Date date = new Date();
        String strDateFormat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
        // 设置时区为东 8 区
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        String dateTimeStr = sdf.format(date);

        String clientIp = IpUtil.getIpAddress(request);
        StringBuilder sb = new StringBuilder();
        sb.append("<h1>Homepage</h1> <p> Current time is ")
        .append(dateTimeStr).append(". Your IP is ")
        .append(clientIp)
        .append(".</p><p><a href=\"/hello\">hello</a></p>");
        return sb.toString();
	}

    @GetMapping("/hello")
    public String hello(){
        return "Hello World 世界你好！<p><a href=\"/\">Back to homepage.</a></p>";
    }
}