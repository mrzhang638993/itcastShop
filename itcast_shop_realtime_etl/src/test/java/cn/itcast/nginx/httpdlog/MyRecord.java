package cn.itcast.nginx.httpdlog;

import lombok.Getter;
import lombok.Setter;
import nl.basjes.parse.core.Field;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

public class MyRecord{
        @Getter
        @Setter
        private String connectionClientUser = null;
        @Getter @Setter private String connectionClientHost = null;
        @Getter @Setter private String requestReceiveTime   = null;
        @Getter @Setter private String method               = null;
        @Getter @Setter private String referrer             = null;
        @Getter @Setter private String screenResolution     = null;
        @Getter @Setter private String requestStatus        = null;
        @Getter @Setter private String responseBodyBytes    = null;
        @Getter @Setter private Long   screenWidth          = null;
        @Getter @Setter private Long   screenHeight         = null;
        @Getter @Setter private String googleQuery          = null;
        @Getter @Setter private String bui                  = null;
        @Getter @Setter private String useragent            = null;

        @Getter @Setter private String asnNumber            = null;
        @Getter @Setter private String asnOrganization      = null;
        @Getter @Setter private String ispName              = null;
        @Getter @Setter private String ispOrganization      = null;

        @Getter @Setter private String continentName        = null;
        @Getter @Setter private String continentCode        = null;
        @Getter @Setter private String countryName          = null;
        @Getter @Setter private String countryIso           = null;
        @Getter @Setter private String subdivisionName      = null;
        @Getter @Setter private String subdivisionIso       = null;
        @Getter @Setter private String cityName             = null;
        @Getter @Setter private String postalCode           = null;
        @Getter @Setter private Double locationLatitude     = null;
        @Getter @Setter private Double locationLongitude    = null;

        private final Map<String, String> results = new HashMap<>(32);

        @Field("STRING:request.firstline.uri.query.*")
        public void setQueryDeepMany(final String name, final String value) {
            results.put(name, value);
        }

        @Field("STRING:request.firstline.uri.query.img")
        public void setQueryImg(final String name, final String value) {
            results.put(name, value);
        }

        @Field("IP:connection.client.host")
        public void setIP(final String value) {
            results.put("IP:connection.client.host", value);
        }

        public String getUser() {
            return results.get("IP:connection.client.host");
        }


        @Field({
                "STRING:connection.client.user",
                "HTTP.HEADER:request.header.addr",
                "IP:connection.client.host.last",
                "TIME.STAMP:request.receive.time.last",
                "TIME.DAY:request.receive.time.last.day",
                "TIME.MONTHNAME:request.receive.time.last.monthname",
                "TIME.MONTH:request.receive.time.last.month",
                "TIME.WEEK:request.receive.time.last.weekofweekyear",
                "TIME.YEAR:request.receive.time.last.weekyear",
                "TIME.YEAR:request.receive.time.last.year",
                "TIME.HOUR:request.receive.time.last.hour",
                "TIME.MINUTE:request.receive.time.last.minute",
                "TIME.SECOND:request.receive.time.last.second",
                "TIME.MILLISECOND:request.receive.time.last.millisecond",
                "TIME.MICROSECOND:request.receive.time.last.microsecond",
                "TIME.NANOSECOND:request.receive.time.last.nanosecond",
                "TIME.DATE:request.receive.time.last.date",
                "TIME.TIME:request.receive.time.last.time",
                "TIME.ZONE:request.receive.time.last.timezone",
                "TIME.EPOCH:request.receive.time.last.epoch",
                "TIME.DAY:request.receive.time.last.day_utc",
                "TIME.MONTHNAME:request.receive.time.last.monthname_utc",
                "TIME.MONTH:request.receive.time.last.month_utc",
                "TIME.WEEK:request.receive.time.last.weekofweekyear_utc",
                "TIME.YEAR:request.receive.time.last.weekyear_utc",
                "TIME.YEAR:request.receive.time.last.year_utc",
                "TIME.HOUR:request.receive.time.last.hour_utc",
                "TIME.MINUTE:request.receive.time.last.minute_utc",
                "TIME.SECOND:request.receive.time.last.second_utc",
                "TIME.MILLISECOND:request.receive.time.last.millisecond_utc",
                "TIME.MICROSECOND:request.receive.time.last.microsecond_utc",
                "TIME.NANOSECOND:request.receive.time.last.nanosecond_utc",
                "TIME.DATE:request.receive.time.last.date_utc",
                "TIME.TIME:request.receive.time.last.time_utc",
                "HTTP.URI:request.referer",
                "HTTP.PROTOCOL:request.referer.protocol",
                "HTTP.USERINFO:request.referer.userinfo",
                "HTTP.HOST:request.referer.host",
                "HTTP.PORT:request.referer.port",
                "HTTP.PATH:request.referer.path",
                "HTTP.QUERYSTRING:request.referer.query",
                "STRING:request.referer.query.*",
                "HTTP.REF:request.referer.ref",
                "TIME.STAMP:request.receive.time",
                "TIME.DAY:request.receive.time.day",
                "TIME.MONTHNAME:request.receive.time.monthname",
                "TIME.MONTH:request.receive.time.month",
                "TIME.WEEK:request.receive.time.weekofweekyear",
                "TIME.YEAR:request.receive.time.weekyear",
                "TIME.YEAR:request.receive.time.year",
                "TIME.HOUR:request.receive.time.hour",
                "TIME.MINUTE:request.receive.time.minute",
                "TIME.SECOND:request.receive.time.second",
                "TIME.MILLISECOND:request.receive.time.millisecond",
                "TIME.MICROSECOND:request.receive.time.microsecond",
                "TIME.NANOSECOND:request.receive.time.nanosecond",
                "TIME.DATE:request.receive.time.date",
                "TIME.TIME:request.receive.time.time",
                "TIME.ZONE:request.receive.time.timezone",
                "TIME.EPOCH:request.receive.time.epoch",
                "TIME.DAY:request.receive.time.day_utc",
                "TIME.MONTHNAME:request.receive.time.monthname_utc",
                "TIME.MONTH:request.receive.time.month_utc",
                "TIME.WEEK:request.receive.time.weekofweekyear_utc",
                "TIME.YEAR:request.receive.time.weekyear_utc",
                "TIME.YEAR:request.receive.time.year_utc",
                "TIME.HOUR:request.receive.time.hour_utc",
                "TIME.MINUTE:request.receive.time.minute_utc",
                "TIME.SECOND:request.receive.time.second_utc",
                "TIME.MILLISECOND:request.receive.time.millisecond_utc",
                "TIME.MICROSECOND:request.receive.time.microsecond_utc",
                "TIME.NANOSECOND:request.receive.time.nanosecond_utc",
                "TIME.DATE:request.receive.time.date_utc",
                "TIME.TIME:request.receive.time.time_utc",
                "HTTP.URI:request.referer.last",
                "HTTP.PROTOCOL:request.referer.last.protocol",
                "HTTP.USERINFO:request.referer.last.userinfo",
                "HTTP.HOST:request.referer.last.host",
                "HTTP.PORT:request.referer.last.port",
                "HTTP.PATH:request.referer.last.path",
                "HTTP.QUERYSTRING:request.referer.last.query",
                "STRING:request.referer.last.query.*",
                "HTTP.REF:request.referer.last.ref",
                "NUMBER:connection.client.logname",
                "BYTESCLF:response.body.bytes",
                "BYTES:response.body.bytes",
                "HTTP.USERAGENT:request.user-agent.last",
                "HTTP.COOKIES:request.cookies.last",
                "HTTP.COOKIE:request.cookies.last.*",
                "STRING:request.status.last",
                "HTTP.USERAGENT:request.user-agent",
                "STRING:connection.client.user.last",
                "HTTP.FIRSTLINE:request.firstline.original",
                "HTTP.METHOD:request.firstline.original.method",
                "HTTP.URI:request.firstline.original.uri",
                "HTTP.PROTOCOL:request.firstline.original.uri.protocol",
                "HTTP.USERINFO:request.firstline.original.uri.userinfo",
                "HTTP.HOST:request.firstline.original.uri.host",
                "HTTP.PORT:request.firstline.original.uri.port",
                "HTTP.PATH:request.firstline.original.uri.path",
                "HTTP.QUERYSTRING:request.firstline.original.uri.query",
                "STRING:request.firstline.original.uri.query.*",
                "HTTP.REF:request.firstline.original.uri.ref",
                "HTTP.PROTOCOL_VERSION:request.firstline.original.protocol",
                "HTTP.PROTOCOL:request.firstline.original.protocol",
                "HTTP.PROTOCOL.VERSION:request.firstline.original.protocol.version",
                "BYTESCLF:response.body.bytes.last",
                "BYTES:response.body.bytes.last",
                "NUMBER:connection.client.logname.last",
                "HTTP.FIRSTLINE:request.firstline",
                "HTTP.METHOD:request.firstline.method",
                "HTTP.URI:request.firstline.uri",
                "HTTP.PROTOCOL:request.firstline.uri.protocol",
                "HTTP.USERINFO:request.firstline.uri.userinfo",
                "HTTP.HOST:request.firstline.uri.host",
                "HTTP.PORT:request.firstline.uri.port",
                "HTTP.PATH:request.firstline.uri.path",
                "HTTP.QUERYSTRING:request.firstline.uri.query",
                "STRING:request.firstline.uri.query.*",
                "HTTP.REF:request.firstline.uri.ref",
                "HTTP.PROTOCOL_VERSION:request.firstline.protocol",
                "HTTP.PROTOCOL:request.firstline.protocol",
                "HTTP.PROTOCOL.VERSION:request.firstline.protocol.version",
                "HTTP.COOKIES:request.cookies",
                "HTTP.COOKIE:request.cookies.*",
                "BYTES:response.body.bytesclf",
                "BYTESCLF:response.body.bytesclf",
                "IP:connection.client.host",
        })
        public void setValue(final String name, final String value) {
            results.put(name, value);
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            TreeSet<String> keys = new TreeSet<>(results.keySet());
            for (String key : keys) {
                sb.append(key).append(" = ").append(results.get(key)).append('\n');
            }

            return sb.toString();
        }

        public void clear() {
            results.clear();
        }
}