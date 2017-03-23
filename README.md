# ContactsDemo
##6.0运行时权限问题
`shouldShowRequestPermissionRationale(Activity activity,String permission)` 
 
 当一个权限第一次被请求和用户标记过不再提醒的时候,我们写的对话框被展示。 
   
 当用户标记不在提醒时，onRequestPermissionsResult会收到PERMISSION_DENIED，系统询问对话框不展示。
 
 小米手机点击决拒绝限默认点击不在提醒
 
##CursorLoader