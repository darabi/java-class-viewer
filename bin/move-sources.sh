#!/bin/bash

for dir in BinaryInternalsViewer  CommonLib  FormatBMP  FormatCLASS  FormatJPEG  FormatPDF  FormatPNG  FormatZIP  JavaClassViewer ; do
    tomove=`ls $dir/src`
    mkdir -p $dir/src/main/java
    for f in $tomove ; do
        git mv $dir/src/$f $dir/src/main/java
    done
done


