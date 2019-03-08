<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false"%>
<html>
<head>
    <title>Title</title>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/plugins/echarts/echarts.common.min.js"></script>
    <%--引入jquery包--%>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/js/plugins/jquery/jquery.min.js"></script>
</head>
<body>
<!-- 为 ECharts 准备一个具备大小（宽高）的 DOM -->
<div id="main" style="width: 600px;height:400px;"></div>
<script type="text/javascript">
    // 基于准备好的dom，初始化echarts实例
    var myChart = echarts.init(document.getElementById('main'));

    // 显示标题，图例和空的坐标轴
    myChart.setOption({
        title: {
            text: '异步数据加载示例'
        },
        tooltip: {},
        legend: {
            data:['销量']
        },
        xAxis: {
            data: []
        },
        yAxis: {},
        series: [{
            name: '销量',
            type: 'bar',
            data: []
        }]
    });

    function loadData() {
        // 异步加载数据
        var url = "${pageContext.request.contextPath}:8081/echarts/getData" ;
        $.get(url).done(function (data) {
            // 填入数据
            myChart.setOption({
                xAxis: {
                    data: data.month
                },
                series: [{
                    // 根据名字对应到相应的系列
                    name: '销量',
                    data: data.totalFee
                }]
            });
        });
    }

    // // 每隔1秒执行一次 异步加载数据
    // setInterval(loadData, 1000);
</script>
</body>
</html>