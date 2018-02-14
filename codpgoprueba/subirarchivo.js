 /* app.controller('SubirArchivos', function($scope, $http) {
          $scope.name = 'World';
          $scope.files = []; 
         // $scope.upload=function(){
         /* this.uploadFileToUrl = function(file, title, text, uploadUrl){
            var fd = new FormData();
            fd.append('file', file);
            var obj = {
              title: title,
              text: text,
              file: fd
            };
            var newObj = JSON.stringify(obj);
         
              $http.post(uploadUrl, newObj, {
                transformRequest: angular.identity,
                headers: {'Content-Type': 'multipart/form-data'}
              })
           .success(function(){
             blockUI.stop();
           })
           .error(function(error){
             toaster.pop('error', 'Errore', error);
           });
         }
         */

        /*var payload = new FormData();

        payload.append("title", title);
        payload.append('text', text);
        payload.append('file', file);
    
        return $http({
            url: uploadUrl,
            method: 'POST',
            data: payload,
            //assign content-type as undefined, the browser
            //will assign the correct boundary for us
            headers: { 'Content-Type': undefined},
            //prevents serializing payload.  don't do it.
            transformRequest: angular.identity
        });*/
         
        
        /*$scope.upload=function(){
            $http({
                method: 'POST',
                url: '/upload-file',
                headers: {
                    'Content-Type': 'multipart/form-data'
                },
                data: {
                  
                    upload: $scope.file
                },
                transformRequest: function (data, headersGetter) {
                    var formData = new FormData();
                    angular.forEach(data, function (value, key) {
                        formData.append(key, value);
                    });
    
                    var headers = headersGetter();
                    delete headers['Content-Type'];
    
                    return formData;
                }
            })
            .success(function (data) {
    
            })
            .error(function (data, status) {
    
            });*/



                //funcion para subir archivos aqui
      /*          var totalsizeOfUploadFiles = 0;    
        var select = $('#uploadTable');
        for(var i =0; i<$scope.files.length; i++)
        {           
            var filesizeInBytes = $scope.files[i].size;
            var filesizeInMB = (filesizeInBytes / (1024*1024)).toFixed(2);
            var filename = $scope.files[i].name;
            //alert("File name is : "+filename+" || size : "+filesizeInMB+" MB || size : "+filesizeInBytes+" Bytes");
            if(i<=4)
            {               
                $('#filetd'+i+'').text(filename);
                $('#filesizetd'+i+'').text(filesizeInMB);
            }
            else if(i>4)
                select.append($('<tr id=tr'+i+'><td id=filetd'+i+'>'+filename+'</td><td id=filesizetd'+i+'>'+filesizeInMB+'</td></tr>'));
            totalsizeOfUploadFiles += parseFloat(filesizeInMB);
            $('#totalsize').text(totalsizeOfUploadFiles.toFixed(2)+" MB");
            if(i==0)
                $('#filecount').text("1file");
            else
            {
                var no = parseInt(i) + 1;
                $('#filecount').text(no+"files");
            }                       
        }           
    }*/
   /* var formdata = new FormData();
    $scope.getTheFiles = function ($files) {
        angular.forEach($files, function (value, key) {
            formdata.append(key, value);
        });
    };*/

    // NOW UPLOAD THE FILES.
    $scope.upload = function () {
        var formdata = new FormData();
        //$filter('name')
        //$scope.files[0].name
       /* angular.forEach($scope.files,function(file){
            formdata.append('value',file.value);
            formdata.append('values',files.values);
            formdata.append('data',files.data);
            formdata.append('dataType',files.dataType);
            formdata.append('name',file.name);
            formdata.append('FieldName', file.value)
          });*/
        angular.forEach($scope.files, function (value, key) {
            formdata.append(key, new Blob([value]));
            //formdata.append('name', 'archivoloco');
            /*formdata.append(key, new Blob([JSON.stringify(value)], {
                type: contentType
            }));*/
                //new Blob([value]));
            //'{"name": "Book", "quantity": "12"}'
        });
        var request = {
            method: 'POST',
            url: '/upload',
            data: formdata,
            headers: {
                "Content-Type": undefined
                //'Content-Type': 'multipart/form-data',
                //'boundary' : "simple boundary" 
            }
        };

        // SEND THE FILES.
        $http(request)
            .success(function (d) {
                alert(d);
            })
            .error(function () {
            });
    }
//});


            //alert($scope.files.length+" files selected ... Write your Upload Code"); 
        
          //};
        });
        app.directive('ngFileModel', ['$parse', function ($parse) {
            return {
                restrict: 'A',
                link: function (scope, element, attrs) {
                    var model = $parse(attrs.ngFileModel);
                    var isMultiple = attrs.multiple;
                    var modelSetter = model.assign;
                    element.bind('change', function () {
                        var values = [];
                        angular.forEach(element[0].files, function (item) {
                            var value = {
                               // File Name 
                                name: item.name,
                                //File Size 
                                size: item.size,
                                //File URL to view 
                                url: URL.createObjectURL(item),
                                // File Input Value 
                                _file: item
                            };
                            values.push(value);
                        });
                        scope.$apply(function () {
                            if (isMultiple) {
                                modelSetter(scope, values);
                            } else {
                                modelSetter(scope, values[0]);
                            }
                        });
                    });
                }
            };
        }]);
