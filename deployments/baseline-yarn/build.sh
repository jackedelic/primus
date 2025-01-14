#!/usr/bin/env bash

#
# Copyright 2022 Bytedance Inc.
#
# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
#    http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

# Environment settings
set -euo pipefail
cd "$(dirname "$0")"/../..
echo -e "Workspace: $(pwd)"

PACKAGE_ONLY="false"
SKIP_TESTS="false"

while [[ $# -gt 0 ]]; do
  case $1 in
    --package-only)
      PACKAGE_ONLY="true"
      shift
      ;;
    --skip-tests)
      SKIP_TESTS="true"
      shift
      ;;
    -*|--*)
      echo "Unknown option $1"
      exit 1
      ;;
  esac
done

# Compile Primus jar
if [[ $PACKAGE_ONLY != "true" ]]; then
  if [[ $SKIP_TESTS == "true" ]]; then
    mvn clean package -DskipTests -P stable
  else
    mvn clean package -P stable
  fi
fi

# Packaging Primus on EMR
rm -rf output
rm -rf *.tgz

mkdir -p output/jars
cp -f runtime/runtime-yarn-community/target/primus-yarn-community-STABLE.jar output/jars/primus-STABLE.jar

mkdir -p output/conf
cp -f deployments/baseline-yarn/conf/* output/conf

mkdir -p output/sbin
cp deployments/baseline-yarn/sbin/* output/sbin

mkdir -p output/resources
cp -rf deployments/baseline-yarn/resources/* output/resources

mkdir -p output/examples
cp -r examples/* output/examples

mkdir -p output/tests
cp -rf deployments/baseline-yarn/tests/* output/tests
