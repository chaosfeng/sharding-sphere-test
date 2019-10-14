/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.feng.shardingjdbctest.service;

import com.feng.shardingjdbctest.exception.MyException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class SagaTransactionService {

    @Autowired
    OrderService orderService;

    /**
     * process success.
     */
    @ShardingTransactionType(TransactionType.BASE)
    @Transactional
    public void processSuccess() {
        log.info("-------------- Process Success Begin ---------------");
        orderService.add(0, 0);
        orderService.add(0, 1);
        orderService.add(1, 0);
        orderService.add(1, 1);
        log.info("-------------- Process Success Finish --------------");
    }

    /**
     * process failure.
     */
    @ShardingTransactionType(TransactionType.BASE)
    @Transactional
    public void processFailure() {
        log.info("-------------- Process Failure Begin ---------------");
        orderService.add(0, 0);
        orderService.add(0, 1);
        orderService.add(1, 0);
        orderService.add(1, 1);
        orderService.add(2, 2);
        orderService.add(2, 3);
        orderService.add(3, 2);
        orderService.add(3, 3);
        log.info("-------------- Process Failure Finish --------------");
        throw new MyException("Exception occur for saga transaction test.");
    }

}
