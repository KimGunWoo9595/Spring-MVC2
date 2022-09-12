package hello.typeconverter.converter;

import hello.typeconverter.type.IpPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
@Slf4j
public class StringToIpPortConverter implements Converter<String, IpPort> {


    @Override   //문자열이 들어오면 IpPort객체로 바꿔주기위함
    public IpPort convert(String source) {
        log.info("convert source={}",source);
        //"123.0.0.1:8080"
        String[] split = source.split(":");

        String ip = split[0];
        int port = Integer.parseInt(split[1]);

        return new IpPort(ip,port);
    }
}
