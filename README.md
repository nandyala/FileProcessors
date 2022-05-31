This Project is a test project.

We have two smaples
1) a flat file reader processor and writer
2) A pipe delimited and multiple recordtype file


 java -jar -DinputFile=C:\\cmc\\input2.txt -DoutputFile=C:\\cmc\\outputFile.txt -DerrorFile=C:\\cmc\\errorFile11111.txt  FileReader.jar



prodStartTime="123900"
prodEndTime="124500"

currentTime=`date +"%H%M%S"`
echo $prodStartTime
echo $prodEndTime
echo $currentTime

if [[ ! ( "$currentTime" < "$prodStartTime" || "$currentTime" > "$prodEndTime" ) ]]; then
   echo "--->"
fi


if [[ $var == "true" ]]
  then
    # get extension
    echo Need to decrypt...

  else
    echo No need for decrypting.
fi
