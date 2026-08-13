#!/usr/bin/env bash
# 一鍵執行所有範例(需要 Java 23,預覽功能範例會加 --enable-preview)
set -euo pipefail
cd "$(dirname "$0")"

JAVA="${JAVA:-java}"

version=$("$JAVA" -version 2>&1 | head -1)
echo "使用 Java:$version"
case "$version" in
  *\"23*) ;;
  *) echo "警告:建議使用 Java 23 執行(預覽功能範例可能失敗)";;
esac

run()         { echo; echo "===== $1 ====="; "$JAVA" "$1"; }
run_preview() { echo; echo "===== $1(--enable-preview)====="; "$JAVA" --enable-preview "$1"; }

run          01-hello/Hello.java
run_preview  01-hello/HelloSimple.java
run          02-basics/Basics.java
run          03-oop/OopDemo.java
run          04-api/ApiDemo.java
run          04-api/VirtualThreadsDemo.java
run          05-java23/MarkdownDocDemo.java
run_preview  05-java23/PrimitivePatternsDemo.java
run_preview  05-java23/GatherersDemo.java
run_preview  05-java23/ModuleImportDemo.java
run_preview  05-java23/FlexibleConstructorDemo.java

echo
echo "===== 06-gc/GcDemo.java(比較四種 GC)====="
for flags in "" "-XX:+UseZGC" "-XX:+UseParallelGC" "-XX:+UseSerialGC"; do
  echo "--- java $flags -Xmx512m GcDemo.java ---"
  # shellcheck disable=SC2086
  "$JAVA" $flags -Xmx512m 06-gc/GcDemo.java
done

echo
echo "全部範例執行完畢 ✅"
