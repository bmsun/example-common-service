package com.example.commons.utils;

import com.example.commons.utils.security.Des;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
@Slf4j
public class ReqUrlUtilTest {

    String cookies;
    String url;
    String body;
    ContentType type = ContentType.APPLICATION_FORM_URLENCODED;
    Map<String, String> headers = Maps.newHashMap();

    @Test
    public void testPost() {
        for (int i=0; ;i++){
            url = "http://iservice.10010.com/e3/static/query/callDetail?_=1528941745728&accessURL=http://iservice.10010.com/e4/query/bill/call_dan-iframe.html&menuid=000100030001";
            body="pageNo=1&pageSize=20&beginDate=20180601&endDate=20180614";
            cookies="_n3fa_cid=954e3501f3d8489f81d2031984966e27; _n3fa_ext=ft=1521192173; piw=%7B%22login_name%22%3A%22185****8512%22%2C%22nickName%22%3A%22%E8%91%A3%E4%BA%9A%E5%86%9B%22%2C%22rme%22%3A%7B%22ac%22%3A%22%22%2C%22at%22%3A%22%22%2C%22pt%22%3A%2201%22%2C%22u%22%3A%2218576638512%22%7D%2C%22verifyState%22%3A%22%22%7D; mallflag=null; SHOP_PROV_CITY=; route=709ce134261dc27c92aa22e8cb3d956f; _n3fa_lvt_a9e72dfe4a54a20c3d6e671b3bad01d9=1528941529,1528964666; _n3fa_lpvt_a9e72dfe4a54a20c3d6e671b3bad01d9=1528964666; _uop_id=de2f4333cbbef52407e51b2dd19c4d84; mallcity=36|360; citycode=360; userprocode=036; WT_FPC=id=21d683a05a6828a774a1528941700968:lv=1528964674965:ss=1528964674959; loginflag=true; e3=Pd32bvmTZyp2h1myGjV879d81QZhjhXS6711jWhRFNgnYbCynhfr!-380798517; JUT=9sJUWIicAeWJaD5RWWw00UA6IlAxHS0lQdoRCQR4fwvMzLpNJUQJ6Gu+SWzJW7nslH6rMzYcyL6YWu8dhUmcesJ/9yIMzqImnIXkQhPfHkFh0FckQ4fqPlRBFh37jOiXgcIUn2EUaiCACTxthWt5grDmO5HNiYk3XcNbROAa77XoOLVfe9p0srxYTQkzhR3pIGwJohN8eDCaXtzYui36+o4YNUjvlDh2sjwYPd4ZCsuYVtgudSKYCEiRLIghPsieNEtHOu6abEtGcgDumNaEB6LWdUSUbKqEFBvq9sljvFmHkvegf/omRyuJDoBWj/PavcE0Lh8GCPyH+mUpeN3JzK1Sz+6SLIZbmft820+AcooGHNVun3U5VFayPid1vxj+AB3Zxn/+EHPQubjmPWXaUnICHA+GT8IWd/UUi0/ZZ11xGH8+tK7OPof1iEsjVa8EYsJQvUmZF4oKp1KdVeQ57NCdqKRVPpUA9UJVFU9CSb+RKlCtFq/mIoLNX6QkSFluz0PAOFLEUq36mHazTBQNGJrApefQrcRClwnmZ3TWLun5tXgAyA0Oi+GWEox00mZM6ZGKA45Eu4M=vKAN55jtNyrK82M5Sxgvsg==; MII=000100030001";
           headers.put("Referer","http://iservice.10010.com/e4/query/bill/call_dan-iframe.html");
            String response = ReqUrlUtil.postMethod(url, body, type, headers, cookies);
            //System.out.println(response);
            log.info("response:[{}]",response);
            if (StringUtils.isBlank(response)||!response.contains("董亚军")){
                break;
            }
            try {
                TimeUnit.MINUTES.sleep(25L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }


    }

    @Test
    public void testShaanXiGet() {
        for (int i=0; ;i=i+5){
            Double random = Math.random() * 100000000;
           String tokenId = String.valueOf(random.intValue());
            url = "http://wap.sn.10086.cn/h5/server/DetailedQuery/queryAllResult?MONTH=201804&BILL_TYPE=201&ajaxSubmitType=post&ajax_randomcode="+tokenId;
            url=shaanxiDes(url,tokenId);
            cookies="cityCodeCookie=571; zjCityCode=571; last_success_login_mobile=18800649710; uv_id=4601439751928046633|2206668; _ca_tk=jz9aczjjs0jt7xfrz16sube0fn38dcjc; fj_areaCode=595; jsessionid-echd-cpt-cmcc-jt=0E32049F727379FBEB4C3566CD9A0266; continue=https://service.bj.10086.cn/poffice/jsp/service/fee/fee.jsp; continuelogout=https://service.bj.10086.cn/poffice/jsp/service/fee/fee.jsp; SESSION_ID=ZjfqSZIcG2ydfP_F-QGLx4L; CaptchaCode=iurpUp; lgToken=9701b603c8e24f53bd1a01986aa97c4e; defaultloginuser_p=%7b%22loginType%22%3a%2201%22%2c%22provinceName%22%3a%22240%22%2c%22pwdType%22%3a%2201%22%2c%22userName%22%3a%2215940718467%22%7d; verifyCode=fef4c182ecafd80eb311763c4e6eb5a0411a92b9; is_login=true; key4IE89=2813AE42EDF991AFF77575B65E0C72155B1CA8A1DE4CA24CBD0379C212C74F29DBAB688FFD0570B66B93838B6B254892; TRACEID=02*20180606160426*000007*newweb*815634; userinfokey=%7b%22loginType%22%3a%2201%22%2c%22provinceName%22%3a%22731%22%2c%22pwdType%22%3a%2201%22%7d; c=e6725fb850ff4ab58cb840b98b13bb07; cmtokenid=\"f3933099c59c43a5a6edb41da078783b@nx.ac.10086.cn\"; CmWebtokenid=\"15709610956,nx\"; LoginWebtokenAccount=15709610956; %2Fservice%2Findex.action=1528287714073; logoin=1528287714074; CmLocation=951|951; CmProvid=nx; my_login_account=15709610956; %2Fmy%2FqryCBillDt.action=1528287724540; %2Fmy%2FqueryBillDetail_detailBill.action=1528287812744; jeesite.session.id=e10286d4e6264b7fbc4407cf9fbade02; uid=112; COOKIE_TELNUM=18392080017; WT_FPC=id=23fd9256d6bbe7b70ad1528186644497:lv=1528446513286:ss=1528443392190";
            headers.put("Referer","http://wap.sn.10086.cn/h5/personal/html/detailedinfo.html?num=201&month=201804");
            System.out.println(url);
            String response = ReqUrlUtil.getMethod(url, headers, cookies);
           // System.out.println(response);
            log.info("response:[{}]",response);
            if (!response.contains("主叫")){
                break;
            }
            try {
                TimeUnit.MINUTES.sleep(5L+i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    @Test
    public void testGet() {
        for (int i=0; ;i=i+5){
            url = "https://m.client.10010.com/mobileService/view/client/query/xdcx/thxd_more_list.jsp?1=1&t=1528689664485&beginrow=80&endrow=113&pagenum=3";
            cookies="cw_mutual=7064d003eb3c8934e769e430ecf3d64ab2d7e7a22ed519bafa8666911a88954e67142d938348339743c34d97fe223d5c6c7a3c14f5cb40c71ba0e4cc20935ea8; a_token=eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1MjkyOTQxNzEsInRva2VuIjp7ImxvZ2luVXNlciI6IjE4NjY4MDE4MDk3IiwicmFuZG9tU3RyIjoieWgxNGI0MjUxNTI4Njg5MzcxIn0sImlhdCI6MTUyODY4OTM3MX0.70bQanRHROpUJnF92uXVPKOhWVpZlMocSkvS-iLO5IwqrvCGir-iEyXBVEidlDyNwlCjAQc3VBa3B6ifcwfbTQ; c_id=978234ddd20e4552e77ee0c475a895ff7fbfe2755f63c8baf224f61f2c1a47b8; c_mobile=18668018097; c_version=iphone_c@5.8; d_deviceCode=4F0CCAB1-DF6D-4F60-86F1-F87976255202; enc_acc=Tp1wwicR1PHBuTr8tMNzedpLV3bsoZAB/kOkAkAp7Na7DjEgUb2d8c1uycrrSbB660OSUO34rj7myAJjD65Klp0PQBcsVBYdpuRYv6ut7+yHvVQm1gUY0H//aCtcQh+MwFolTBWkyRtahdIj18qJNRKk4n6Zg3qMTPd37xZ0JZA=; invalid_at=cba7f5afbba79fd7a2fd6a32e0b093521203ef39c33f7d7366380535a3632938; random_login=0; t3_token=0d44a8d002b607a3cab47658002502db; third_token=eyJkYXRhIjoiMTMyYzJlNGFmOTFiOWU0ZTRmMmMyMDQwOWVkNWU5NDI3ZjA0NjA1YzdhZDBlNmFjMTc4NTE3MjkzNDhhNDVmNzgyZWI0MGRjNzUyNWY4Mzg1Yjg1ZjE3ZmM1ZTRmY2EzMzNhYWM0Mjk1YjE5NmRlZjM1YzYyNDI1MDc5ZmQxZWZjYWVkYmIxZThkNGYyNmQxNjFmMmQ1ODg3Y2IxYWUyNiIsInZlcnNpb24iOiIwMCJ9; u_account=18668018097; u_areaCode=360; u_type=01; c_sfbm=4g_1; clientid=36|360; req_mobile=18668018097; req_serial=; req_wheel=ssss; city=036|360; mobileService=l5DTbdybXLrY35PlFzvR24QTJYDhlvNVGvxPF25MQLPRQNDsgcJQ!1708738825; mobileServicecb=8649fba29c8c2c52d2542d5114c4496b; route=55f54410e8db5be50a8dbc21f26a6e18; mobileServiceOther=0a9747dcb45b8d065b9a54c5f1014a45";
            headers.put("Referer","https://m.client.10010.com/mobileService/query/getPhoneByDetailTip.htm?menuId=000200030001&mobile_c_from=query");
            String response = ReqUrlUtil.getMethod(url, headers, cookies);
            // System.out.println(response);
            log.info("response:[{}]",response);
            if (!response.contains("2分53秒")){
                break;
            }
            try {
                TimeUnit.MINUTES.sleep(25L+i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    @Test
    public void testPageDetailGet() {
        for (int i=0; ;i=i+5){
            url = "https://m.client.10010.com/mobileService/view/client/query/xdcx/thxd_more_list.jsp?1=1&t=1528689664485&beginrow=80&endrow=113&pagenum=3";
            cookies="cw_mutual=7064d003eb3c8934e769e430ecf3d64ab2d7e7a22ed519bafa8666911a88954e67142d938348339743c34d97fe223d5c6c7a3c14f5cb40c71ba0e4cc20935ea8; a_token=eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1MjkzNzQxNzgsInRva2VuIjp7ImxvZ2luVXNlciI6IjE4NjY4MDE4MDk3IiwicmFuZG9tU3RyIjoieWgwM2U1NjExNTI4NzY5Mzc4In0sImlhdCI6MTUyODc2OTM3OH0.YaLicZG9W35qIH0EgWjjqwbO2rol03KDtGR2VRVQpk3tGRFWLrddhuzyBqAyYpvk-lYs_ZB_d9M6WVbz-Px44Q; c_id=978234ddd20e4552e77ee0c475a895ff8e3664fe41b2dd361ce8066869b35b41; c_mobile=18668018097; c_version=iphone_c@5.8; d_deviceCode=4F0CCAB1-DF6D-4F60-86F1-F87976255202; enc_acc=ZSGes9j2x+LnfarRAOj2zjVlj86jXov8y6NsMAp8EebgAaMO624nlBZJmXLUN004jl8niToxLaic3ZhcMeQDNMsZtyCkD2Mg65QGoUtxlLtKiN5XY0XSzVdZaGJTmKzxqq1rw0VYVEhYqv4yWnB7vMgavwJHJulrSMb3NZ49ieI=; invalid_at=a6e279f66c301d60cf4abcb3e3c0ca4828a7521c1d23920b2a55f6032d83f5e3; random_login=0; t3_token=bc873c6b8bc80d275b0a597e5e46348c; third_token=eyJkYXRhIjoiMTMyYzJlNGFmOTFiOWU0ZTRmMmMyMDQwOWVkNWU5NDJjMjA2NzMxNWEyM2Y0ODI2YTQyNGZjZDdjNjdiNTQ4NWE1MzI4ZTc2MTQ0MTZjYjg5Y2Q1NTkyMDEzN2I1Yjk4NzI3MjE3MjNjMWQyNDc1OWRjMDBkMzNlODVmNTBlNmJjMzZjMGQwOTkyNjdiNmIxMmRlMWQ2YjU3NzI5NzdmMiIsInZlcnNpb24iOiIwMCJ9; u_account=18668018097; u_areaCode=360; u_type=01; c_sfbm=4g_1; city=036|360; clientid=36|360; req_mobile=18668018097; req_serial=; req_wheel=ssss; mobileService=Dx8dbfrBg5fxKZxbPHv1FnRd7jhBmBTpznk211Sy7sV0Lh7XNJQp!1777341676; mobileServicecb=fbc1c6ba98e5ae30de14d482fa1d7df6; route=1cb499e1e9387df9f55a2f1c6c297bca; mobileServiceOther=348d8ae879ed96174edecfdabb48263e";
            headers.put("Referer","https://m.client.10010.com/mobileService/query/getPhoneByDetailTip.htm?menuId=000200030001&mobile_c_from=query");
            String response = ReqUrlUtil.getMethod(url, headers, cookies);
            // System.out.println(response);
            log.info("response:[{}]",response);
            if (!response.contains("2分53秒")){
                break;
            }
            try {
                TimeUnit.MINUTES.sleep(25L+i*2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    @Test
    public void testUnicomDetailPost() {
        for (int i=0; ;i++){
            long time=System.currentTimeMillis();
            url = "https://m.client.10010.com/mobileService/query/getPhoneByDetailContent.htm";
            body="menuId=000200030001&t="+time;
            cookies="cw_mutual=7064d003eb3c8934e769e430ecf3d64ab2d7e7a22ed519bafa8666911a88954e67142d938348339743c34d97fe223d5c6c7a3c14f5cb40c71ba0e4cc20935ea8; a_token=eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1MjkyOTQxNzEsInRva2VuIjp7ImxvZ2luVXNlciI6IjE4NjY4MDE4MDk3IiwicmFuZG9tU3RyIjoieWgxNGI0MjUxNTI4Njg5MzcxIn0sImlhdCI6MTUyODY4OTM3MX0.70bQanRHROpUJnF92uXVPKOhWVpZlMocSkvS-iLO5IwqrvCGir-iEyXBVEidlDyNwlCjAQc3VBa3B6ifcwfbTQ; c_id=978234ddd20e4552e77ee0c475a895ff7fbfe2755f63c8baf224f61f2c1a47b8; c_mobile=18668018097; c_version=iphone_c@5.8; d_deviceCode=4F0CCAB1-DF6D-4F60-86F1-F87976255202; enc_acc=Tp1wwicR1PHBuTr8tMNzedpLV3bsoZAB/kOkAkAp7Na7DjEgUb2d8c1uycrrSbB660OSUO34rj7myAJjD65Klp0PQBcsVBYdpuRYv6ut7+yHvVQm1gUY0H//aCtcQh+MwFolTBWkyRtahdIj18qJNRKk4n6Zg3qMTPd37xZ0JZA=; invalid_at=cba7f5afbba79fd7a2fd6a32e0b093521203ef39c33f7d7366380535a3632938; random_login=0; t3_token=0d44a8d002b607a3cab47658002502db; third_token=eyJkYXRhIjoiMTMyYzJlNGFmOTFiOWU0ZTRmMmMyMDQwOWVkNWU5NDI3ZjA0NjA1YzdhZDBlNmFjMTc4NTE3MjkzNDhhNDVmNzgyZWI0MGRjNzUyNWY4Mzg1Yjg1ZjE3ZmM1ZTRmY2EzMzNhYWM0Mjk1YjE5NmRlZjM1YzYyNDI1MDc5ZmQxZWZjYWVkYmIxZThkNGYyNmQxNjFmMmQ1ODg3Y2IxYWUyNiIsInZlcnNpb24iOiIwMCJ9; u_account=18668018097; u_areaCode=360; u_type=01; c_sfbm=4g_1; clientid=36|360; req_mobile=18668018097; req_serial=; req_wheel=ssss; city=036|360; mobileService=l5DTbdybXLrY35PlFzvR24QTJYDhlvNVGvxPF25MQLPRQNDsgcJQ!1708738825; mobileServicecb=8649fba29c8c2c52d2542d5114c4496b; route=55f54410e8db5be50a8dbc21f26a6e18; mobileServiceOther=0a9747dcb45b8d065b9a54c5f1014a45";
            //headers.put("Referer","https://m.client.10010.com/mobileService/query/getPhoneByDetailTip.htm?menuId=000200030001&mobile_c_from=query");
            String response = ReqUrlUtil.postMethod(url, body, type, headers, cookies);
            //System.out.println(response);
            log.info("response:[{}]",response);
            if (!response.contains("17秒")){
                break;
            }
            try {
                TimeUnit.MINUTES.sleep(30L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }


    }



    @Test
    public void test(){
        String response="{\"loginFlag\":false,\"message\":\"-\"}";
        System.out.println(response.contains("\"loginFlag\":false"));
    }


    private String shaanxiDes( String url,String tokenId){
        String AJAX_FLAG = "&ajaxSubmitType=post";
       String START_INDEX = "?";
        int startIndex = url.indexOf(START_INDEX) + 1;
        int endIndex = url.indexOf(AJAX_FLAG);
        String params = url.substring(startIndex, endIndex);
        String newParams = securiteAjax(params, tokenId);
        url = url.replace(params, newParams);
        return url;
    }

    public  String securiteAjax(String params, String securite) {
        if (StringUtils.isBlank(securite)) {
            //* 100000000
            Double random = Math.random() * 100000000;
            securite = String.valueOf(random.intValue());
        }
        String[] splitArr = params.split("&");
        String newParams="";
        for (int i=0;i<splitArr.length;i++){
            int pos = splitArr[i].indexOf("=");
            if (pos==-1) {
                continue;
            }
            //提取name
            String argname=splitArr[i].substring(0,pos);
            //提取value
            String value=splitArr[i].substring(pos+1);
            if(StringUtils.isNotBlank(value)){
                Des des =new Des();
                value = des.strEnc(value, securite, securite, securite);
            }
            newParams+=argname+"="+value;
            if (i<splitArr.length-1){
                newParams+="&";
            }
        }
        return newParams;
    }



    @Test
    public void testBatchGet() {
        String[] moblieArr={"18456047476",
                "13520914429",
                "13896041867",
                "15259728183",
                "18793174132",
                "13534026259",
                "18776146009",
                "13765017910",
                "15808948246",
                "15031287507",
                "13846290449",
                "13700779552",
                "13429801680",
                "18390871856",
                "18800649710",
                "15079355987",
                "15043928105",
                "15940718467",
                "15124713317",
                "15709610956",
                "15009736791",
                "18392080017",
                "13964802880",
                "13774247563",
                "13453126943",
                "18228304912",
                "15822195914",
                "13899263508",
                "15089017654",
                "15288403844",
                "15224099137",
                "17755830541",
                "17319468780",
                "18983350357",
                "18105014909",
                "18909460764",
                "18123552191",
                "18172649927",
                "18085083949",
                "13379903486",
                "13383624290",
                "18145576326",
                "17739233316",
                "17364253290",
                "17373488433",
                "15366428816",
                "18007933370",
                "13364485701",
                "17704178672",
                "18947186824",
                "17395583835",
                "15309735643",
                "18092282249",
                "17762012984",
                "18116273323",
                "15392634547",
                "17313153037",
                "18002137621",
                "18099743274",
                "17740780355",
                "13368893390",
                "18142018269",
                "18523176776",
                "15533025928",
                "13091652538",
                "18507908103",
                "15643060617",
                "18642944446",
                "15608130199",
                "15687255446",
                "18668018097",
                "18576638512",
                "17091122333"};
        List<String> list = Arrays.asList(moblieArr);
        for (String moblie:list){
           url = "http://192.168.0.75:4072/tenant/config/v2/carrier/field/findpwd/"+ moblie;
           headers.put("Accept","application/json");
           headers.put("Referer","https://m.client.10010.com/mobileService/query/getPhoneByDetailTip.htm?menuId=000200030001&mobile_c_from=query");
           headers.put("Authorization","apikey 6149056c09e1498ca9b1bcd534b5ad0c");
           headers.put("Cache-Control","no-cache");
           headers.put("Content-Type","application/json");
           headers.put("Postman-Token","3a29cb24-bb82-435c-8b51-1d0caeb31219");
           headers.put("X-Moxie-Param","AAAAXgQ9KE3o5BDdnXh4V73Ba4Ca7QqviQJDKOYptuB4OEi96+ViMcv18toDfcuSgDMcE2ap8Fds7SnwT3Y9h/qD1lN1Heg43R61DBsZ3/CkKh3eGurD/cojXRemKP0zOwKOpw==.QVZtaVNZVEk4eGgxUS9oMXB6RGM0OXk5RUVBeC8xT29pbU9IN2VNUWRVVE1seloxemt6UktzV29pUmVPQnZHTkY5MUlrM0ptM285Y3l4UDNPa0wwWUZrMStZK21Zck9KUGpzWVh4YVhZR3VMd2d6aUJUaVBIekphZVc4VCsvYkVsOU15YjJuMzJtendGbGpXVnE1OXV6eXVHY000WmFUREc3MllGOTRJNmg4PQ==");
           String response = ReqUrlUtil.getMethod(url, headers, cookies);
            //log.info("response:[{}]",response);
           if(response==null){
               log.info("mobile:[{}]->调用接口异常",moblie);
           }
            if (!response.contains("操作成功")){
                log.info("mobile:[{}]->调用接口返回非正常信息",moblie);
                break;
            }
            try {
                TimeUnit.SECONDS.sleep(1L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }






}
