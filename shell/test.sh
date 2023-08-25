 #!/bin/bash
 if [ $# -eq 0 ]
 then
 echo "pass an argument !"
 else
 for i in $*
 do
 if [ -f $i ]
 then

 a=`echo $i | tr '[a-z]' '[A-Z]' `
 mv $i $a
 echo "New file name is: $a"
 else
 echo "Not a file"
 fi
 done
 fi
