package org.grade.listeners;

import cn.hutool.core.util.BooleanUtil;
import lombok.extern.slf4j.Slf4j;
import org.grade.bean.dto.ScoreDTO;
import org.grade.model.Region;
import org.grade.service.IRegionService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class CallbackListener {
    @Resource
    private IRegionService regionService;
    @Resource
    private SimpMessagingTemplate messagingTemplate;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "callback.queue", durable = "true"),
            exchange = @Exchange(name = "intel.fanout", type = ExchangeTypes.FANOUT)
    ))
    public void callback(ScoreDTO scoreDTO) {
        boolean update = regionService.lambdaUpdate()
                .eq(Region::getRegionId, scoreDTO.getRegionId())
                .set(Region::getRegionScore, scoreDTO.getScore())
                .set(Region::getIsGraded, true)
                .set(Region::getRegionContent, scoreDTO.getContent()).update();

        if (BooleanUtil.isFalse(update)) {
            throw new RuntimeException("评分细则保存失败");
        }

        // 将结果发送到 WebSocket 客户端
        messagingTemplate.convertAndSend("/topic/messages", scoreDTO);
    }
}
