//购物车控制层
app.controller('seckillCartController',function($scope,seckillCartService){
	//查询购物车列表
	$scope.findSeckillOrder=function(){
		seckillCartService.findSeckillOrder().success(
			function(response){
				$scope.seckillOrder=response;
				// $scope.totalValue= seckillCartService.sum($scope.cartList);
			}
		);
	}
    // //查询收藏列表
    // $scope.findCartList=function(){
    //     cartService.findCartList().success(
    //         function(response){
    //             $scope.cartList=response;
    //             $scope.totalValue= cartService.sum($scope.cartList);
    //         }
    //     );
    // }



	
	// //数量加减
	// $scope.addGoodsToCartList=function(itemId,num){
	// 	cartService.addGoodsToCartList(itemId,num).success(
	// 		function(response){
	// 			if(response.success){//如果成功
	// 				$scope.findCartList();//刷新列表
	// 			}else{
	// 				alert(response.message);
	// 			}
	// 		}
	// 	);
	// }
	

	
	//获取当前用户的地址列表
	$scope.findAddressList=function(){
		seckillCartService.findAddressList().success(
			function(response){
				$scope.addressList=response;
				for(var i=0;i<$scope.addressList.length;i++){
					if($scope.addressList[i].isDefault=='1'){
						$scope.address=$scope.addressList[i];
						break;
					}					
				}
				
			}
		);		
	}
	
	//选择地址
	$scope.selectAddress=function(address){
		$scope.address=address;		
	}
	
	//判断某地址对象是不是当前选择的地址
	$scope.isSeletedAddress=function(address){
		if(address==$scope.address){
			return true;
		}else{
			return false;
		}		
	}
	
	$scope.order={paymentType:'1'};//订单对象
	
	//选择支付类型
	$scope.selectPayType=function(type){
		$scope.order.paymentType=type;
	}
	
	//保存订单
	$scope.submitOrder=function(){
		$scope.seckillOrder.receiverAddress=$scope.address.address;//地址
        $scope.seckillOrder.receiverMobile=$scope.address.mobile;//手机
		$scope.seckillOrder.receiver=$scope.address.contact;//联系人
		
		seckillCartService.submitOrder( $scope.seckillOrder ).success(
			function(response){
				//alert(response.message);
				if(response.success){
					//页面跳转

						location.href="pay.html";

					
				}else{
					alert(response.message);	//也可以跳转到提示页面				
				}
				
			}				
		);		
	}
	
});