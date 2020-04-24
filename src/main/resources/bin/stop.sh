#!/usr/bin/env bash
ps -ef | grep java | grep futurestrategy | cut -c 9-15 | xargs kill -9