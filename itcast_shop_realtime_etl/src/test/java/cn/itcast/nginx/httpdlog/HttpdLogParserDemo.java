package cn.itcast.nginx.httpdlog;

import nl.basjes.parse.core.Parser;
import nl.basjes.parse.core.exceptions.DissectionFailure;
import nl.basjes.parse.core.exceptions.InvalidDissectorException;
import nl.basjes.parse.core.exceptions.MissingDissectorsException;
import nl.basjes.parse.httpdlog.HttpdLoglineParser;

import java.util.List;

public class HttpdLogParserDemo {

    public static void main(String[] args) throws MissingDissectorsException, NoSuchMethodException, DissectionFailure, InvalidDissectorException {
        new HttpdLogParserDemo().run();
    }

    String logformat = "%u %h %l %t \"%r\" %>s %b \"%{Referer}i\" \"%{User-Agent}i\" \"%{Cookie}i\" \"%{Addr}i\"";
    String logline = "2001-980:91c0:1:8d31:a232:25e5:85d 222.68.172.190 - [05/Sep/2010:11:27:50 +0200] \"GET /images/my.jpg HTTP/1.1\" 404 23617 \"http://www.angularjs.cn/A00n\" \"Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_4; nl-nl) AppleWebKit/533.17.8 (KHTML, like Gecko) Version/5.0.1 Safari/533.17.8\" \"jquery-ui-theme=Eggplant; BuI=SomeThing; Apache=127.0.0.1.1351111543699529\" \"beijingshi\"";

    private void run() throws InvalidDissectorException, MissingDissectorsException, NoSuchMethodException, DissectionFailure {
        //打印日志格式的参数列表
        printAllPossibles(logformat);

        //将日志参数映射成对象
        Parser<MyRecord> parser = new HttpdLoglineParser<>(MyRecord.class, logformat);

        parser.addParseTarget("setConnectionClientUser", "STRING:connection.client.user");
        parser.addParseTarget("setConnectionClientHost", "IP:connection.client.host");
        parser.addParseTarget("setRequestReceiveTime", "TIME.STAMP:request.receive.time");
        parser.addParseTarget("setMethod", "HTTP.METHOD:request.firstline.method");
        parser.addParseTarget("setRequestStatus", "STRING:request.status.last");
        parser.addParseTarget("setScreenResolution", "HTTP.URI:request.firstline.uri");
        parser.addParseTarget("setResponseBodyBytes", "BYTES:response.body.bytes");
        parser.addParseTarget("setReferrer", "HTTP.URI:request.referer");
        parser.addParseTarget("setUseragent", "HTTP.USERAGENT:request.user-agent");

        MyRecord record = new MyRecord();
        System.out.println("==============================================");
        parser.parse(record, logline);
        //LOG.info(record.toString());
        System.out.println(record.toString());
        System.out.println("==============================================");
        System.out.println(record.getConnectionClientUser());
        System.out.println(record.getConnectionClientHost());
        System.out.println(record.getRequestReceiveTime());
        System.out.println(record.getMethod());
        System.out.println(record.getScreenResolution());
        System.out.println(record.getRequestStatus());
        System.out.println(record.getResponseBodyBytes());
        System.out.println(record.getReferrer());
        System.out.println(record.getUseragent());
    }

    //打印所有参数
    private void printAllPossibles(String logformat) throws NoSuchMethodException, MissingDissectorsException, InvalidDissectorException {
        Parser<Object> dummyParser = new HttpdLoglineParser<>(Object.class, logformat);
        List<String> possiblePaths = dummyParser.getPossiblePaths();
        dummyParser.addParseTarget(String.class.getMethod("indexOf", String.class), possiblePaths);
        System.out.println("==================================");
        System.out.println("Possible output:");
        for (String path : possiblePaths) {
            System.out.println(path + "     " + dummyParser.getCasts(path));
        }
        System.out.println("==================================");
    }
}
