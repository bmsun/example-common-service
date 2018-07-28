package com.example.commons.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

@Slf4j
public class XinJiangWebResponseTest {
    private final String BILL_NAME="billName";
    private final String BILL_FEE="billFee";
    private final String SPECIAL_FLAG="specialFlag";

    private final String BRACE_TOP_FLAG="{";
    private final String BRACE_BACK_FLAG="}";
    private final String COLON_FLAG=":";
    private final String COMMA_FLAG=",";
    @Test
    public void test(){
        String content="{\"resultMsg\":\"查询成功\",\"billName\":[\"套餐及固定费\",\"基本月租(全月)\",\"套餐月基本费\",\"语音通话费\",\"上网费\",\"短信/彩信费\",\"短信息网内点对点费\",\"电信点对点短信通信费\",\"增值业务费\",\"手机气象信息费\",\"来电管家功能费\",\"代收费\",\"其他费用\"],\"itemFlag\":[\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\"],\"fee2\":[\"1800\",\"800\",\"1000\",\"0\",\"0\",\"40\",\"10\",\"30\",\"400\",\"100\",\"300\",\"0\",\"0\"],\"billBalance\":[\"18.00\",\"8.00\",\"10.00\",\"0.00\",\"0.00\",\"0.40\",\"0.10\",\"0.30\",\"4.00\",\"1.00\",\"3.00\",\"0.00\",\"0.00\"],\"specialFlag\":[\"1\",\"2\",\"2\",\"1\",\"1\",\"1\",\"2\",\"2\",\"1\",\"2\",\"2\",\"1\",\"1\"],\"TokenValid\":\"20180515090822\",\"resultCode\":\"0\",\"billCode\":[\"1\",\"10008\",\"10977\",\"2\",\"3\",\"4\",\"11012\",\"11016\",\"5\",\"10030\",\"10134\",\"6\",\"7\"],\"sumBalance\":\"22.40\",\"g_result_info\":\"successInfo起始日期尊敬的神州行客户：您目前没有查找到相应记录请选择其它月份，欢迎使用网上营业厅自助服务。成功充值成功服务号码业务办理提示账目金额没有按正常流程操作余额费用真实姓名不能办理此业务不能办理此业务账目名称您当前的账户余额;依赖;gzip;不提供;error;error_code=-8;升级;欠费;您还没有登录.\"}";
        if (StringUtils.isNotBlank(content)){
            String billNames = RegexUtils.regexPattern("\"billName\":\\[(.*?)\\]", content,1);
            log.info("billNames:[{}]",billNames);
            String[] billNameArr = billNames.split(",");

            String specialFlags = RegexUtils.regexPattern("\"specialFlag\":\\[(.*?)\\]", content,1);
            log.info("specialFlags:[{}]",specialFlags);
            String[] specialFlagArr = specialFlags.split(",");

            String billFees = RegexUtils.regexPattern("\"billBalance\":\\[(.*?)\\]", content,1);
            log.info("billFees:[{}]",billFees);
            String[] billFeeArr = billFees.split(",");

          StringBuilder newContents =new StringBuilder();
          if (billNameArr.length==billFeeArr.length&&billFeeArr.length==specialFlagArr.length){
              for (int i=0;i<billFeeArr.length;i++){
                  StringBuilder singlebill=new StringBuilder(BRACE_TOP_FLAG);
                  singlebill.append(String.format("%s%s%s%s",BILL_NAME,COLON_FLAG,billNameArr[i],COMMA_FLAG));
                  singlebill.append(String.format("%s%s%s%s",BILL_FEE,COLON_FLAG,billFeeArr[i],COMMA_FLAG));
                  singlebill.append(String.format("%s%s%s%s",SPECIAL_FLAG,COLON_FLAG,specialFlagArr[i],BRACE_BACK_FLAG));
                  newContents.append(singlebill);
                  if (i<billFeeArr.length-1){
                      newContents.append(COMMA_FLAG);
                  }
              }
          }
            System.out.println(newContents);

        }
    }
}
