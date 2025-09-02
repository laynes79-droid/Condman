#!/usr/bin/env sh
#
# Copyright 2015 the original author or authors.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      https://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
DEFAULT_JVM_OPTS=""
APP_NAME="Gradle"
APP_BASE_NAME=`basename "$0"`
MAX_FD="maximum"
cygwin=false
darwin=false
mingw=false
case "`uname`" in
  CYGWIN*)
    cygwin=true
    ;;
  Darwin*)
    darwin=true
    ;;
  MINGW*)
    mingw=true
    ;;
esac
if ${cygwin} ; then
  [ -n "$GRADLE_HOME" ] && GRADLE_HOME=`cygpath --unix "$GRADLE_HOME"`
  [ -n "$JAVA_HOME" ] && JAVA_HOME=`cygpath --unix "$JAVA_HOME"`
fi
PRG="$0"
while [ -h "$PRG" ] ; do
  ls=`ls -ld "$PRG"`
  link=`expr "$ls" : '.*-> \(.*\)$'`
  if expr "$link" : '/.*' > /dev/null; then
    PRG="$link"
  else
    PRG=`dirname "$PRG"`"/$link"
  fi
done
SAVED="`pwd`"
cd "`dirname \"$PRG\"`" >/dev/null
APP_HOME="`pwd -P`"
cd "$SAVED" >/dev/null
if [ -z "$JAVA_HOME" ]; then
    if [ -n "$GRADLE_JAVA_HOME" ]; then
        if [ -d "$GRADLE_JAVA_HOME" ]; then
            JAVA_HOME="$GRADLE_JAVA_HOME"
        else
            echo "Warning: GRADLE_JAVA_HOME is set to an invalid directory: $GRADLE_JAVA_HOME"
            echo
        fi
    fi
fi
if [ -z "$JAVA_HOME" ]; then
    if [ -d "/opt/jdk" ]; then
        JAVA_HOME=`find /opt/jdk -maxdepth 1 -type d -name "jdk*" | sort -r | head -n 1`
    fi
fi
if [ -z "$JAVA_HOME" ]; then
    if [ -d "/usr/lib/jvm" ]; then
        JAVA_HOME=`find /usr/lib/jvm -maxdepth 1 -type d -name "java-*" | sort -r | head -n 1`
    fi
fi
if [ -n "$JAVA_HOME" ] ; then
  if [ -x "$JAVA_HOME/jre/sh/java" ] ; then
    JAVACMD="$JAVA_HOME/jre/sh/java"
  else
    JAVACMD="$JAVA_HOME/bin/java"
  fi
  if [ ! -x "$JAVACMD" ] ; then
    die "ERROR: JAVA_HOME is set to an invalid directory: $JAVA_HOME

Please set the JAVA_HOME variable in your environment to match the
location of your Java installation."
  fi
else
  JAVACMD="java"
  which java >/dev/null 2>&1 || die "ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.

Please set the JAVA_HOME variable in your environment to match the
location of your Java installation."
fi
if ! ${cygwin} && ! ${darwin} ; then
  if [ "$MAX_FD" = "maximum" -o "$MAX_FD" = "max" ] ; then
    MAX_FD_LIMIT=`ulimit -H -n`
    if [ $? -eq 0 ] ; then
      if [ "$MAX_FD_LIMIT" != "unlimited" ] ; then
        ulimit -n $MAX_FD_LIMIT
      fi
    else
      echo "Could not query system maximum file descriptor limit: $MAX_FD_LIMIT"
    fi
  else
    if [ "$MAX_FD" != "-1" ] ; then
      ulimit -n $MAX_FD
    fi
  fi
fi
if ${cygwin} ; then
  APP_HOME=`cygpath --path --windows "$APP_HOME"`
  CLASSPATH=`cygpath --path --windows "$CLASSPATH"`
fi
function splitJvmOpts() {
  JVM_OPTS=()
  for opt in $1; do
    opt=${opt#\"}
    opt=${opt%\"}
    opt=${opt#\'}
    opt=${opt%\'}
    JVM_OPTS+=("$opt")
  done
}
splitJvmOpts "$DEFAULT_JVM_OPTS"
DEFAULT_JVM_OPTS=("${JVM_OPTS[@]}")
splitJvmOpts "$GRADLE_OPTS"
GRADLE_OPTS=("${JVM_OPTS[@]}")
splitJvmOpts "$JAVA_OPTS"
JAVA_OPTS=("${JVM_OPTS[@]}")
function collectArgs() {
    local _java_args=()
    local _classpath=
    local _main_class=
    local _app_args=()
    _java_args+=("-Dorg.gradle.appname=$APP_BASE_NAME")
    for arg in "$@"; do
        if [ -z "$_main_class" ]; then
            if [[ "$arg" =~ ^-D.+$ || "$arg" =~ ^-X.+$ || "$arg" =~ ^-agentlib:.+$ || "$arg" =~ ^-agentpath:.+$ || "$arg" =~ ^-javaagent:.+$ ]]; then
                _java_args+=("$arg")
            elif [ "$arg" = "-classpath" ] || [ "$arg" = "-cp" ]; then
                _classpath="placeholder"
            elif [ -z "$_classpath" ]; then
                _classpath="$arg"
            else
                _main_class="$arg"
            fi
        else
            _app_args+=("$arg")
        fi
    done
    if [ -n "$CLASSPATH" ]; then
        if [ -n "$_classpath" ]; then
            _classpath="$CLASSPATH:$_classpath"
        else
            _classpath="$CLASSPATH"
        fi
    fi
    echo "${_java_args[@]}" "$_classpath" "$_main_class" "${_app_args[@]}"
}
if [ -f "$APP_HOME/bin/gradle" ]; then
    rm "$APP_HOME/bin/gradle"
fi
function getScriptDir() {
    local _script_dir
    _script_dir="$( cd "$( dirname "$1" )" && pwd )"
    echo "$_script_dir"
}
function getGradleWrapperJar() {
    local _gradle_wrapper_jar
    _gradle_wrapper_jar="$(getScriptDir "$0")/gradle/wrapper/gradle-wrapper.jar"
    echo "$_gradle_wrapper_jar"
}
function getGradleWrapperProperties() {
    local _gradle_wrapper_properties
    _gradle_wrapper_properties="$(getScriptDir "$0")/gradle/wrapper/gradle-wrapper.properties"
    echo "$_gradle_wrapper_properties"
}
function getGradleWrapperProperty() {
    local _property_name="$1"
    local _property_value
    if [ -f "$(getGradleWrapperProperties)" ]; then
        _property_value=$(grep -E "^${_property_name}=" "$(getGradleWrapperProperties)" | cut -d'=' -f2)
    fi
    echo "$_property_value"
}
function getGradleWrapperDistributionUrl() {
    local _distribution_url
    _distribution_url=$(getGradleWrapperProperty "distributionUrl")
    echo "$_distribution_url"
}
function getGradleWrapperDistributionBase() {
    local _distribution_base
    _distribution_base=$(getGradleWrapperProperty "distributionBase")
    echo "$_distribution_base"
}
function getGradleWrapperDistributionPath() {
    local _distribution_path
    _distribution_path=$(getGradleWrapperProperty "distributionPath")
    echo "$_distribution_path"
}
function getGradleWrapperZipStoreBase() {
    local _zip_store_base
    _zip_store_base=$(getGradleWrapperProperty "zipStoreBase")
    echo "$_zip_store_base"
}
function getGradleWrapperZipStorePath() {
    local _zip_store_path
    _zip_store_path=$(getGradleWrapperProperty "zipStorePath")
    echo "$_zip_store_path"
}
function downloadGradleWrapperDistribution() {
    local _distribution_url="$(getGradleWrapperDistributionUrl)"
    local _distribution_base="$(getGradleWrapperDistributionBase)"
    local _distribution_path="$(getGradleWrapperDistributionPath)"
    local _zip_store_base="$(getGradleWrapperZipStoreBase)"
    local _zip_store_path="$(getGradleWrapperZipStorePath)"
    if [ -z "$_distribution_url" ]; then
        echo "No distribution URL found in $(getGradleWrapperProperties)"
        return 1
    fi
    local _distribution_dir
    if [ "$_distribution_base" = "GRADLE_USER_HOME" ]; then
        _distribution_dir="$HOME/.gradle/wrapper/dists"
    elif [ "$_distribution_base" = "PROJECT" ]; then
        _distribution_dir="$(getScriptDir "$0")/gradle/wrapper/dists"
    else
        echo "Unknown distribution base: $_distribution_base"
        return 1
    fi
    local _distribution_path_in_dir
    if [ -n "$_distribution_path" ]; then
        _distribution_path_in_dir="$_distribution_dir/$_distribution_path"
    else
        _distribution_path_in_dir="$_distribution_dir/$(basename "$_distribution_url")"
    fi
    local _zip_store_dir
    if [ "$_zip_store_base" = "GRADLE_USER_HOME" ]; then
        _zip_store_dir="$HOME/.gradle/wrapper/dists"
    elif [ "$_zip_store_base" = "PROJECT" ]; then
        _zip_store_dir="$(getScriptDir "$0")/gradle/wrapper/dists"
    else
        echo "Unknown zip store base: $_zip_store_base"
        return 1
    fi
    local _zip_store_path_in_dir
    if [ -n "$_zip_store_path" ]; then
        _zip_store_path_in_dir="$_zip_store_dir/$_zip_store_path"
    else
        _zip_store_path_in_dir="$_zip_store_dir/$(basename "$_distribution_url")"
    fi
    if [ ! -d "$_distribution_path_in_dir" ]; then
        mkdir -p "$_distribution_path_in_dir"
    fi
    if [ ! -d "$_zip_store_path_in_dir" ]; then
        mkdir -p "$_zip_store_path_in_dir"
    fi
    local _zip_file="$_zip_store_path_in_dir/$(basename "$_distribution_url")"
    if [ ! -f "$_zip_file" ]; then
        echo "Downloading $_distribution_url"
        if command -v "curl" > /dev/null; then
            curl -L -o "$_zip_file" "$_distribution_url"
        elif command -v "wget" > /dev/null; then
            wget -O "$_zip_file" "$_distribution_url"
        else
            echo "Neither curl nor wget found. Please install one and try again."
            return 1
        fi
    fi
    local _unzip_dir="$_distribution_path_in_dir/$(basename "$_distribution_url" .zip)"
    if [ ! -d "$_unzip_dir" ]; then
        echo "Unzipping $_zip_file to $_unzip_dir"
        if command -v "unzip" > /dev/null; then
            unzip -q -d "$_unzip_dir" "$_zip_file"
        else
            echo "unzip not found. Please install it and try again."
            return 1
        fi
    fi
    local _gradle_home="$_unzip_dir/$(ls "$_unzip_dir" | head -n 1)"
    if [ -d "$_gradle_home" ]; then
        export GRADLE_HOME="$_gradle_home"
    else
        echo "Could not find gradle home in $_unzip_dir"
        return 1
    fi
    return 0
}
function main() {
    downloadGradleWrapperDistribution
    if [ $? -ne 0 ]; then
        exit 1
    fi
    local _java_args
    _java_args=$(collectArgs "$@")
    exec "$JAVACMD" "${DEFAULT_JVM_OPTS[@]}" "${GRADLE_OPTS[@]}" "${JAVA_OPTS[@]}" -cp "$(getGradleWrapperJar)" "org.gradle.wrapper.GradleWrapperMain" $_java_args
}
main "$@"
