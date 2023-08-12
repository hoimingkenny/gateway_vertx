package com.bean.handler;

import com.bean.OrderCmdContainer;
import com.thirdparty.bean.CommonMsg;
import com.thirdparty.codec.IBodyCodec;
import com.thirdparty.order.OrderCmd;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor
@Log4j2
public class MsgHandlerImpl implements IMsgHandler {

    private IBodyCodec bodyCodec;

    @Override
    public void onCounterData(CommonMsg msg) {
        OrderCmd orderCmd;

        try {
            orderCmd = bodyCodec.deserialize(msg.getBody(), OrderCmd.class);
            log.info("recv cmd: {}",orderCmd);

//            if(log.isDebugEnabled()){
//              log.debug("recv cmd: {}",orderCmd);
//            }
            if(!OrderCmdContainer.getInstance().cache(orderCmd)){
                log.error("gateway queue insert fail,queue length:{},order:{}",
                        OrderCmdContainer.getInstance().size(),
                        orderCmd);
            }
        } catch (Exception e) {
            // business logic exceptions should not be thrown to the upper level
            // handle here
            log.error("decode order cmd error", e);
        }
    }
}
