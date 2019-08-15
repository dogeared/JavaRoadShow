package com.afitnerd.jjwt.microservices.controller;

import com.afitnerd.jjwt.microservices.model.JWTResponse;
import com.afitnerd.jjwt.microservices.service.SecretService;
import com.afitnerd.jjwt.microservices.service.SpringBootKafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
public class MessagingMicroServiceController extends BaseController {

    private SpringBootKafkaProducer springBootKafkaProducer;

    public MessagingMicroServiceController(SecretService secretService) {
        super(secretService);
    }

    @Autowired(required=false)
    public void setSpringBootKafkaProducer(SpringBootKafkaProducer springBootKafkaProducer) {
        this.springBootKafkaProducer = springBootKafkaProducer;
    }

    private static final Logger log = LoggerFactory.getLogger(MessagingMicroServiceController.class);

    @RequestMapping("/msg-account-request")
    public JWTResponse authBuilder(@RequestBody Map<String, Object> claims) throws ExecutionException, InterruptedException {
        String jwt = createJwt(claims);

        if (springBootKafkaProducer != null) {
            springBootKafkaProducer.send(jwt);
        } else {
            log.warn("Kafka is disabled.");
        }

        return new JWTResponse(jwt);
    }
}
