/*
 * Copyright 2022 Bytedance Inc.
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bytedance.primus.am.datastream.file.operator.op;

import com.bytedance.primus.am.datastream.file.operator.Input;
import com.bytedance.primus.common.collections.Pair;
import java.util.List;

public interface MapPartitions<T extends Input> extends Operator {

  /**
   * Apply a function to the value of a pair.
   *
   * @param input
   * @return
   */
  Pair<String, List<T>> apply(Pair<String, List<T>> input);
}