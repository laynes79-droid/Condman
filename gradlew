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

# Add default JVM options here. You can also use JAVA_OPTS and GRADLE_OPTS to pass any JVM options to Gradle.
DEFAULT_JVM_OPTS=""

APP_NAME="Gradle"
APP_BASE_NAME=`basename "$0"`

# Use the maximum available, or set MAX_FD != -1 to use that value.
MAX_FD="maximum"

# OS specific support.
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

# For Cygwin, ensure paths are in UNIX format before anything is touched.
if ${cygwin} ; then
  [ -n "$GRADLE_HOME" ] && GRADLE_HOME=`cygpath --unix "$GRADLE_HOME"`
  [ -n "$JAVA_HOME" ] && JAVA_HOME=`cygpath --unix "$JAVA_HOME"`
fi

# Attempt to set APP_HOME
# Resolve links: $0 may be a link
PRG="$0"
# Need this for relative symlinks.
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

# Attempt to set JAVA_HOME if it is not set
if [ -z "$JAVA_HOME" ]; then
    # If we have a hint, try to use it.
    if [ -n "$GRADLE_JAVA_HOME" ]; then
        if [ -d "$GRADLE_JAVA_HOME" ]; then
            JAVA_HOME="$GRADLE_JAVA_HOME"
        else
            # GRADLE_JAVA_HOME is not a valid directory.
            # We will ignore it.
            echo "Warning: GRADLE_JAVA_HOME is set to an invalid directory: $GRADLE_JAVA_HOME"
            echo
        fi
    fi
fi
if [ -z "$JAVA_HOME" ]; then
    # Look for the JDK in the standard locations.
    if [ -d "/opt/jdk" ]; then
        # Use the latest JDK in /opt/jdk
        JAVA_HOME=`find /opt/jdk -maxdepth 1 -type d -name "jdk*" | sort -r | head -n 1`
    fi
fi
if [ -z "$JAVA_HOME" ]; then
    # Look for the JDK in the standard locations.
    if [ -d "/usr/lib/jvm" ]; then
        # Use the latest JDK in /usr/lib/jvm
        JAVA_HOME=`find /usr/lib/jvm -maxdepth 1 -type d -name "java-*" | sort -r | head -n 1`
    fi
fi

# Determine the Java command to use to start the JVM.
if [ -n "$JAVA_HOME" ] ; then
  if [ -x "$JAVA_HOME/jre/sh/java" ] ; then
    # IBM's JDK on AIX uses strange locations for the executables
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

# Increase the maximum number of open files if necessary.
if ! ${cygwin} && ! ${darwin} ; then
  if [ "$MAX_FD" = "maximum" -o "$MAX_FD" = "max" ] ; then
    # Use the maximum available.
    MAX_FD_LIMIT=`ulimit -H -n`
    if [ $? -eq 0 ] ; then
      # We have a limit, try to use it.
      if [ "$MAX_FD_LIMIT" != "unlimited" ] ; then
        ulimit -n $MAX_FD_LIMIT
      fi
    else
      echo "Could not query system maximum file descriptor limit: $MAX_FD_LIMIT"
    fi
  else
    # Use the value of MAX_FD.
    if [ "$MAX_FD" != "-1" ] ; then
      ulimit -n $MAX_FD
    fi
  fi
fi

# For Cygwin, switch paths to Windows format before running java
if ${cygwin} ; then
  APP_HOME=`cygpath --path --windows "$APP_HOME"`
  CLASSPATH=`cygpath --path --windows "$CLASSPATH"`
fi

# Split up the JVM options string into an array, following the shell quoting and substitution rules
function splitJvmOpts() {
  JVM_OPTS=()
  for opt in $1; do
    # Remove surrounding double quotes
    opt=${opt#\"}
    opt=${opt%\"}
    # Remove surrounding single quotes
    opt=${opt#\'}
    opt=${opt%\'}
    JVM_OPTS+=("$opt")
  done
}

# Add default JVM options, if any
splitJvmOpts "$DEFAULT_JVM_OPTS"
DEFAULT_JVM_OPTS=("${JVM_OPTS[@]}")

# Add GRADLE_OPTS, if any
splitJvmOpts "$GRADLE_OPTS"
GRADLE_OPTS=("${JVM_OPTS[@]}")

# Add JAVA_OPTS, if any
splitJvmOpts "$JAVA_OPTS"
JAVA_OPTS=("${JVM_OPTS[@]}")

# Collect all arguments for the java command, following the shell quoting and substitution rules
#
# (This is a simplified algorithm that assumes that options do not contain whitespace.)
#
# @param... all arguments for the java command
# @return the classpath, the main class and the arguments for the main class
#
function collectArgs() {
    local _java_args=()
    local _classpath=
    local _main_class=
    local _app_args=()

    # The following logic is used to signal to the executed Java process that it is being run by this script.
    _java_args+=("-Dorg.gradle.appname=$APP_BASE_NAME")

    # The main class is taken from the first non-empty argument that is not a JVM option.
    # All subsequent arguments are passed to the main class.
    for arg in "$@"; do
        if [ -z "$_main_class" ]; then
            if [[ "$arg" =~ ^-D.+$ || "$arg" =~ ^-X.+$ || "$arg" =~ ^-agentlib:.+$ || "$arg" =~ ^-agentpath:.+$ || "$arg" =~ ^-javaagent:.+$ ]]; then
                _java_args+=("$arg")
            elif [ "$arg" = "-classpath" ] || [ "$arg" = "-cp" ]; then
                # The next argument is the classpath.
                # The "-classpath" option is removed from the arguments.
                _classpath="placeholder"
            elif [ -z "$_classpath" ]; then
                # The classpath is passed as the next argument.
                # The argument is removed from the arguments.
                _classpath="$arg"
            else
                # The main class is the first argument that is not a JVM option and not a classpath.
                _main_class="$arg"
            fi
        else
            # All subsequent arguments are passed to the main class.
            _app_args+=("$arg")
        fi
    done

    # The classpath is built from the "-classpath" option and the CLASSPATH environment variable.
    if [ -n "$CLASSPATH" ]; then
        if [ -n "$_classpath" ]; then
            _classpath="$CLASSPATH:$_classpath"
        else
            _classpath="$CLASSPATH"
        fi
    fi

    # The classpath, the main class and the arguments are returned
    echo "${_java_args[@]}" "$_classpath" "$_main_class" "${_app_args[@]}"
}

# Remove the old Gradle executable, if it exists
if [ -f "$APP_HOME/bin/gradle" ]; then
    rm "$APP_HOME/bin/gradle"
fi

# The directory of the script
#
# (This is a simplified algorithm that assumes that the script is not a symlink.)
#
# @return the directory of the script
#
function getScriptDir() {
    local _script_dir
    # The directory is taken from the first argument.
    _script_dir="$( cd "$( dirname "$1" )" && pwd )"
    echo "$_script_dir"
}

# The path to the gradle wrapper jar
#
# @return the path to the gradle wrapper jar
#
function getGradleWrapperJar() {
    local _gradle_wrapper_jar
    # The path is built from the script directory and the gradle wrapper jar name.
    _gradle_wrapper_jar="$(getScriptDir "$0")/gradle/wrapper/gradle-wrapper.jar"
    echo "$_gradle_wrapper_jar"
}

# The path to the gradle wrapper properties
#
# @return the path to the gradle wrapper properties
#
function getGradleWrapperProperties() {
    local _gradle_wrapper_properties
    # The path is built from the script directory and the gradle wrapper properties name.
    _gradle_wrapper_properties="$(getScriptDir "$0")/gradle/wrapper/gradle-wrapper.properties"
    echo "$_gradle_wrapper_properties"
}

# The value of a property in the gradle wrapper properties
#
# @param the name of the property
# @return the value of the property
#
function getGradleWrapperProperty() {
    local _property_name="$1"
    local _property_value
    # The value is taken from the gradle wrapper properties.
    if [ -f "$(getGradleWrapperProperties)" ]; then
        _property_value=$(grep -E "^${_property_name}=" "$(getGradleWrapperProperties)" | cut -d'=' -f2)
    fi
    echo "$_property_value"
}

# The distribution URL of the gradle wrapper
#
# @return the distribution URL of the gradle wrapper
#
function getGradleWrapperDistributionUrl() {
    local _distribution_url
    # The distribution URL is taken from the gradle wrapper properties.
    _distribution_url=$(getGradleWrapperProperty "distributionUrl")
    echo "$_distribution_url"
}

# The distribution base of the gradle wrapper
#
# @return the distribution base of the gradle wrapper
#
function getGradleWrapperDistributionBase() {
    local _distribution_base
    # The distribution base is taken from the gradle wrapper properties.
    _distribution_base=$(getGradleWrapperProperty "distributionBase")
    echo "$_distribution_base"
}

# The distribution path of the gradle wrapper
#
# @return the distribution path of the gradle wrapper
#
function getGradleWrapperDistributionPath() {
    local _distribution_path
    # The distribution path is taken from the gradle wrapper properties.
    _distribution_path=$(getGradleWrapperProperty "distributionPath")
    echo "$_distribution_path"
}

# The zip store base of the gradle wrapper
#
# @return the zip store base of the gradle wrapper
#
function getGradleWrapperZipStoreBase() {
    local _zip_store_base
    # The zip store base is taken from the gradle wrapper properties.
    _zip_store_base=$(getGradleWrapperProperty "zipStoreBase")
    echo "$_zip_store_base"
}

# The zip store path of the gradle wrapper
#
# @return the zip store path of the gradle wrapper
#
function getGradleWrapperZipStorePath() {
    local _zip_store_path
    # The zip store path is taken from the gradle wrapper properties.
    _zip_store_path=$(getGradleWrapperProperty "zipStorePath")
    echo "$_zip_store_path"
}

# Download the gradle wrapper distribution
#
# @return 0 if the download was successful, 1 otherwise
#
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

    # The directory where the distribution is downloaded
    local _distribution_dir
    if [ "$_distribution_base" = "GRADLE_USER_HOME" ]; then
        _distribution_dir="$HOME/.gradle/wrapper/dists"
    elif [ "$_distribution_base" = "PROJECT" ]; then
        _distribution_dir="$(getScriptDir "$0")/gradle/wrapper/dists"
    else
        echo "Unknown distribution base: $_distribution_base"
        return 1
    fi

    # The path to the distribution
    local _distribution_path_in_dir
    if [ -n "$_distribution_path" ]; then
        _distribution_path_in_dir="$_distribution_dir/$_distribution_path"
    else
        _distribution_path_in_dir="$_distribution_dir/$(basename "$_distribution_url")"
    fi

    # The directory where the zip is stored
    local _zip_store_dir
    if [ "$_zip_store_base" = "GRADLE_USER_HOME" ]; then
        _zip_store_dir="$HOME/.gradle/wrapper/dists"
    elif [ "$_zip_store_base" = "PROJECT" ]; then
        _zip_store_dir="$(getScriptDir "$0")/gradle/wrapper/dists"
    else
        echo "Unknown zip store base: $_zip_store_base"
        return 1
    fi

    # The path to the zip
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

# The main entry point
#
# @param... all arguments
#
function main() {
    # Download the gradle wrapper distribution
    downloadGradleWrapperDistribution
    if [ $? -ne 0 ]; then
        exit 1
    fi

    # Collect all arguments for the java command
    local _java_args
    _java_args=$(collectArgs "$@")

    # Run the java command
    exec "$JAVACMD" "${DEFAULT_JVM_OPTS[@]}" "${GRADLE_OPTS[@]}" "${JAVA_OPTS[@]}" -cp "$(getGradleWrapperJar)" "org.gradle.wrapper.GradleWrapperMain" $_java_args
}

main "$@"
