#!/bin/bash

base_dir=$(dirname $0)

if [ "x$APP_LOGBACK_OPTS" = "x" ]; then
    export APP_LOGBACK_OPTS="-Dlogback.configurationFile=file:$base_dir/../config/logback.xml"
fi

if [ "x$APP_HEAP_OPTS" = "x" ]; then
    export APP_HEAP_OPTS="-Xmx1G -Xms1G"
fi

EXTRA_ARGS=${EXTRA_ARGS-'-name proxyServer -loggc'}

COMMAND=$1
case $COMMAND in
  -daemon)
    EXTRA_ARGS="-daemon "$EXTRA_ARGS
    shift
    ;;
  *)
    ;;
esac

MAIN_CLASS=pers.lyks.asiant.CliApplication


exec $base_dir/console-run-class.sh $EXTRA_ARGS $MAIN_CLASS "$@"
