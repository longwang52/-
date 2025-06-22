// 全局变量
let communityData = [];
let districtData = [];
let featureData = {};
let investmentData = [];
let priceTrendData = [];
let beijingMapRegistered = false; // 跟踪北京地图是否已注册

// 热力图默认数据
const defaultHeatmapData = [
    { name: '朝阳区', value: 2907 },
    { name: '丰台区', value: 2906 },
    { name: '海淀区', value: 2877 },
    { name: '昌平区', value: 2801 },
    { name: '大兴区', value: 2103 },
    { name: '西城区', value: 1872 },
    { name: '通州区', value: 1596 },
    { name: '东城区', value: 1432 },
    { name: '房山区', value: 1431 },
    { name: '顺义区', value: 1219 },
    { name: '石景山区', value: 836 },
    { name: '门头沟区', value: 479 },
    { name: '延庆区', value: 468 },
    { name: '平谷区', value: 41 },
    { name: '怀柔区', value: 15 },
    { name: '密云区', value: 12 }
];

// 初始化函数
function initDashboard() {
    // 确保 ECharts 已加载
    if (typeof echarts === 'undefined') {
        console.error('ECharts not loaded! Retrying...');
        setTimeout(initDashboard, 100);
        return;
    }

    // 尝试注册北京地图
    registerBeijingMap();

    // 加载所有数据
    loadAllData();

    // 设置基础数据
    setBasicStats();

    // 初始化图表
    initDistrictPriceChart();
    initBeijingHeatmap();
    initPriceVolatilityChart();
    initDistrictDistributionChart();
    initAreaRangeChart();
    initCommunityScroll();

    // 设置定时刷新
    setInterval(loadAllData, 300000);
}

// 尝试注册北京地图
function registerBeijingMap() {
    // 尝试直接使用内置的中国地图
    if (echarts.getMap('china')) {
        beijingMapRegistered = true;
        return;
    }

    // 如果内置地图不可用，尝试加载北京地图
    fetch('echarts-4.9.0/map/js/province/beijing.json')
        .then(response => {
            if (!response.ok) throw new Error('地图加载失败');
            return response.json();
        })
        .then(beijingJson => {
            echarts.registerMap('北京', beijingJson);
            beijingMapRegistered = true;
        })
        .catch(error => {
            console.error('加载北京地图失败:', error);
            // 回退到中国地图
            if (echarts.getMap('china')) {
                beijingMapRegistered = true;
            } else {
                console.warn('无法加载任何地图数据');
            }
        });
}

// 加载所有数据
function loadAllData() {
    fetch('/Second_hand_housing_data_web_war_exploded/district/stats')
        .then(response => response.json())
        .then(data => {
            // 确保区名以"区"结尾
            districtData = data.map(item => ({
                ...item,
                district: item.district.endsWith('区') ? item.district : item.district + '区'
            }));
            updateDistrictCharts();
            setBasicStats();
        })
        .catch(error => console.error('加载区域数据失败:', error));

    fetch('/Second_hand_housing_data_web_war_exploded/community/top')
        .then(response => response.json())
        .then(data => {
            communityData = data;
            updateCommunityScroll();
        })
        .catch(error => console.error('加载小区数据失败:', error));

    fetch('/Second_hand_housing_data_web_war_exploded/feature/stats')
        .then(response => response.json())
        .then(data => {
            featureData = data;
            updateAreaRangeChart();
        })
        .catch(error => console.error('加载特征数据失败:', error));

    fetch('/Second_hand_housing_data_web_war_exploded/investment/potential')
        .then(response => response.json())
        .then(data => {
            investmentData = data;
            updatePriceVolatilityChart();
        })
        .catch(error => console.error('加载投资数据失败:', error));

    fetch('/Second_hand_housing_data_web_war_exploded/price/trend')
        .then(response => response.json())
        .then(data => {
            priceTrendData = data;
        })
        .catch(error => console.error('加载价格趋势数据失败:', error));
}

// 设置基础数据
function setBasicStats() {
    // 默认值
    const defaultTotalHouses = 22995;
    const defaultTotalCommunities = 4371;
    const defaultAvgArea = 113;
    const defaultAvgPricePerSqM = 5.4;

    // 如果没有区域数据，使用默认值
    if (districtData.length === 0) {
        document.getElementById('total-houses').textContent = defaultTotalHouses.toLocaleString();
        document.getElementById('total-communities').textContent = defaultTotalCommunities.toLocaleString();
        document.getElementById('avg-area').textContent = defaultAvgArea.toFixed(2);
        document.getElementById('avg-price').textContent = defaultAvgPricePerSqM.toFixed(2);
        return;
    }

    // 计算总房源数
    const totalHouses = districtData.reduce((sum, district) =>
        sum + (district.house_count || 0), 0);

    // 计算带权重的平均面积和均价
    let weightedTotalArea = 0;
    let weightedTotalPricePerSqM = 0;
    let totalWeight = 0;

    districtData.forEach(district => {
        const houseCount = district.house_count || 0;
        const avgArea = district.avg_area || 0;
        const avgPricePerSqM = district.avg_price_per_sqm || 0;

        weightedTotalArea += avgArea * houseCount;
        weightedTotalPricePerSqM += avgPricePerSqM * houseCount;
        totalWeight += houseCount;
    });

    const avgArea = totalWeight > 0 ? weightedTotalArea / totalWeight : 0;
    const avgPricePerSqM = totalWeight > 0 ? weightedTotalPricePerSqM / totalWeight : 0;

    // 更新页面上的统计数据
    document.getElementById('total-houses').textContent = totalHouses.toLocaleString();
    document.getElementById('avg-area').textContent = avgArea.toFixed(2);
    document.getElementById('avg-price').textContent = avgPricePerSqM.toFixed(2);
    // 小区总数使用默认值（因为接口没有返回小区总数）
    document.getElementById('total-communities').textContent = defaultTotalCommunities.toLocaleString();
}

// 初始化各区均价柱状图
function initDistrictPriceChart() {
    const chartDom = document.getElementById('district-price-chart');
    const myChart = echarts.init(chartDom);

    const option = {
        tooltip: {
            trigger: 'axis',
            axisPointer: { type: 'shadow' },
            formatter: '{b}<br/>均价: {c} 万元/㎡'
        },
        grid: { left: '3%', right: '4%', bottom: '3%', top: '10%', containLabel: true },
        xAxis: {
            type: 'value',
            axisLine: { lineStyle: { color: '#2a3d6b' } },
            axisLabel: { color: '#a7b9d4' },
            splitLine: { lineStyle: { color: '#2a3d6b' } }
        },
        yAxis: {
            type: 'category',
            data: [],
            axisLine: { lineStyle: { color: '#2a3d6b' } },
            axisLabel: { color: '#a7b9d4' }
        },
        series: [{
            name: '均价',
            type: 'bar',
            data: [],
            itemStyle: {
                color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
                    { offset: 0, color: '#2d8cf0' },
                    { offset: 1, color: '#19be6b' }
                ])
            },
            label: {
                show: true,
                position: 'right',
                formatter: '{c} 万',
                color: '#fff'
            }
        }]
    };

    myChart.setOption(option);
    window.addEventListener('resize', () => {
        if (myChart && !myChart.isDisposed()) {
            myChart.resize();
        }
    });
    return myChart;
}

// 初始化北京热力图 - 使用更可靠的方式
function initBeijingHeatmap() {
    const chartDom = document.getElementById('beijing-heatmap');
    const myChart = echarts.init(chartDom);

    // 基本配置，不依赖地图数据
    const option = {
        tooltip: {
            trigger: 'item',
            formatter: '{b}<br/>房源数量: {c}'
        },
        visualMap: {
            min: 0,
            max: 3000, // 修改上限为3000
            text: ['高', '低'],
            realtime: false,
            calculable: true,
            inRange: {
                // 修改为：绿、黄、橙、红（从高到低）
                color: ['#00b050', '#ffd400', '#f47920', '#d71345']
            },
            textStyle: { color: '#a7b9d4' }
        },
        series: [{
            name: '房源数量',
            type: 'map',
            mapType: '北京', // 尝试使用北京地图
            roam: true,
            label: { show: true, color: '#fff' },
            emphasis: {
                label: { color: '#fff' },
                itemStyle: { areaColor: '#ffd400' } // 高亮时使用黄色
            },
            data: defaultHeatmapData // 设置默认数据
        }]
    };

    myChart.setOption(option);
    window.addEventListener('resize', () => {
        if (myChart && !myChart.isDisposed()) {
            myChart.resize();
        }
    });
    return myChart;
}

// 初始化价格波动图
function initPriceVolatilityChart() {
    const chartDom = document.getElementById('price-volatility-chart');
    const myChart = echarts.init(chartDom);

    const option = {
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'cross',
                label: { backgroundColor: '#6a7985' }
            }
        },
        legend: {
            data: [],
            textStyle: { color: '#a7b9d4' },
            top: '0%'
        },
        grid: { left: '3%', right: '4%', bottom: '3%', top: '15%', containLabel: true },
        xAxis: {
            type: 'category',
            boundaryGap: false,
            data: [],
            axisLine: { lineStyle: { color: '#2a3d6b' } },
            axisLabel: { color: '#a7b9d4' }
        },
        yAxis: {
            type: 'value',
            axisLine: { lineStyle: { color: '#2a3d6b' } },
            axisLabel: {
                color: '#a7b9d4',
                formatter: '{value}%'
            },
            splitLine: { lineStyle: { color: '#2a3d6b' } }
        },
        series: []
    };

    myChart.setOption(option);
    window.addEventListener('resize', () => {
        if (myChart && !myChart.isDisposed()) {
            myChart.resize();
        }
    });
    return myChart;
}

// 初始化各区房源分布图
function initDistrictDistributionChart() {
    const chartDom = document.getElementById('district-distribution-chart');
    const myChart = echarts.init(chartDom);

    // 添加加载动画
    myChart.showLoading({
        text: '正在加载数据...',
        color: '#2d8cf0',
        textColor: '#a7b9d4',
        maskColor: 'rgba(42, 61, 107, 0.8)'
    });

    const option = {
        tooltip: {
            trigger: 'axis',
            axisPointer: { type: 'shadow' },
            formatter: '{b}<br/>房源数量: {c}'
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '10%',  // 增加底部空间，防止标签被截断
            top: '10%',
            containLabel: true
        },
        xAxis: {
            type: 'category',
            data: [],
            axisLine: { lineStyle: { color: '#2a3d6b' } },
            axisLabel: {
                color: '#a7b9d4',
                interval: 0,
                rotate: 45,  // 增加旋转角度，防止标签重叠
                margin: 15, // 增加标签间距
                formatter: function(value) {
                    // 简化长名称
                    return value.length > 3 ? value.substring(0, 2) : value;
                }
            }
        },
        yAxis: {
            type: 'value',
            name: '房源数量',
            nameTextStyle: { color: '#a7b9d4' },
            axisLine: { lineStyle: { color: '#2a3d6b' } },
            axisLabel: { color: '#a7b9d4' },
            splitLine: { lineStyle: { color: '#2a3d6b' } }
        },
        series: [{
            name: '房源数量',
            type: 'bar',
            barWidth: '60%',
            data: [],
            itemStyle: {
                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                    { offset: 0, color: '#f8b551' },
                    { offset: 1, color: '#f2711a' }
                ])
            },
            label: {
                show: true,
                position: 'top',
                color: '#f8b551',
                formatter: '{c}'
            }
        }]
    };

    myChart.setOption(option);
    window.addEventListener('resize', () => {
        if (myChart && !myChart.isDisposed()) {
            myChart.resize();
        }
    });
    return myChart;
}

// 初始化面积区间环形图
function initAreaRangeChart() {
    const chartDom = document.getElementById('area-range-chart');
    const myChart = echarts.init(chartDom);

    const option = {
        tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        legend: {
            orient: 'vertical',
            right: 10,
            top: 'center',
            textStyle: { color: '#a7b9d4' }
        },
        series: [{
            name: '面积区间',
            type: 'pie',
            radius: ['40%', '70%'],
            avoidLabelOverlap: false,
            itemStyle: {
                borderRadius: 10,
                borderColor: '#1a2b50',
                borderWidth: 2
            },
            label: { show: false, position: 'center' },
            emphasis: {
                label: {
                    show: true,
                    fontSize: '18',
                    fontWeight: 'bold',
                    color: '#fff'
                }
            },
            labelLine: { show: false },
            data: []
        }]
    };

    myChart.setOption(option);
    window.addEventListener('resize', () => {
        if (myChart && !myChart.isDisposed()) {
            myChart.resize();
        }
    });
    return myChart;
}

// 初始化小区信息滚动
function initCommunityScroll() {
    // 初始为空，数据加载后更新
}

// 更新各区均价图表
function updateDistrictCharts() {
    // 更新各区均价柱状图
    const districtPriceChart = echarts.getInstanceByDom(document.getElementById('district-price-chart'));
    if (districtPriceChart && !districtPriceChart.isDisposed()) {
        // 如果有数据则使用实际数据，否则使用默认排序
        const sortedData = districtData.length > 0
            ? [...districtData].sort((a, b) => b.avg_price_per_sqm - a.avg_price_per_sqm)
            : [...defaultHeatmapData].sort((a, b) => b.value - a.value);

        districtPriceChart.setOption({
            yAxis: {
                data: sortedData.map(item => item.district || item.name)
            },
            series: [{
                data: sortedData.map(item => item.avg_price_per_sqm || 0)
            }]
        });
    }

    // 更新北京热力图
    const beijingHeatmap = echarts.getInstanceByDom(document.getElementById('beijing-heatmap'));
    if (beijingHeatmap && !beijingHeatmap.isDisposed()) {
        try {
            // 如果有数据则使用实际数据，否则使用默认数据
            const heatmapData = districtData.length > 0
                ? districtData.map(item => ({
                    name: item.district,
                    value: item.house_count
                }))
                : defaultHeatmapData;

            // 如果北京地图未注册，回退到中国地图
            const mapType = beijingMapRegistered ? '北京' : 'china';

            beijingHeatmap.setOption({
                series: [{
                    mapType: mapType,
                    data: heatmapData
                }]
            });
        } catch (e) {
            console.error('更新热力图失败:', e);
            // 热力图不可用，显示备用视图
            document.getElementById('beijing-heatmap').innerHTML = `
                <div class="map-fallback">
                    <p>无法加载热力图</p>
                    <p>各区房源数量:</p>
                    <ul>
                        ${defaultHeatmapData.map(item => `<li>${item.name}: ${item.value}</li>`).join('')}
                    </ul>
                </div>
            `;
        }
    }

    // 更新各区房源分布图
    const districtDistributionChart = echarts.getInstanceByDom(document.getElementById('district-distribution-chart'));
    if (districtDistributionChart && !districtDistributionChart.isDisposed()) {
        // 如果有数据则使用实际数据，否则使用默认数据
        const sortedData = districtData.length > 0
            ? [...districtData].sort((a, b) => b.house_count - a.house_count)
            : [...defaultHeatmapData].sort((a, b) => b.value - a.value);

        districtDistributionChart.hideLoading();
        districtDistributionChart.setOption({
            xAxis: {
                data: sortedData.map(item => item.district || item.name)
            },
            series: [{
                data: sortedData.map(item => item.house_count || item.value)
            }]
        });
    }
}

// 更新价格波动图
function updatePriceVolatilityChart() {
    if (investmentData.length === 0) return;

    // 按区域分组计算平均波动率
    const districtMap = {};
    investmentData.forEach(item => {
        const district = item.district;
        if (!districtMap[district]) {
            districtMap[district] = { count: 0, totalVolatility: 0 };
        }
        const volatility = parseFloat((item.price_volatility || '0').replace(/,/g, ''));
        districtMap[district].count++;
        districtMap[district].totalVolatility += volatility;
    });

    const districts = Object.keys(districtMap);
    const avgVolatility = districts.map(district =>
        (districtMap[district].totalVolatility / districtMap[district].count).toFixed(2)
    );

    const priceVolatilityChart = echarts.getInstanceByDom(document.getElementById('price-volatility-chart'));
    if (priceVolatilityChart && !priceVolatilityChart.isDisposed()) {
        priceVolatilityChart.setOption({
            legend: {
                data: ['平均价格波动率']
            },
            xAxis: {
                data: districts
            },
            series: [{
                name: '平均价格波动率',
                type: 'line',
                smooth: true,
                lineStyle: { width: 3, color: '#f8b551' },
                areaStyle: {
                    color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                        { offset: 0, color: 'rgba(248, 181, 81, 0.5)' },
                        { offset: 1, color: 'rgba(248, 181, 81, 0.1)' }
                    ])
                },
                emphasis: { focus: 'series' },
                data: avgVolatility,
                markPoint: {
                    data: [
                        { type: 'max', name: '最大值' },
                        { type: 'min', name: '最小值' }
                    ]
                },
                markLine: { data: [{ type: 'average', name: '平均值' }] }
            }]
        });
    }
}

// 更新面积区间环形图
function updateAreaRangeChart() {
    if (!featureData.areaRanges || featureData.areaRanges.length === 0) return;

    // 准备环形图数据
    const pieData = featureData.areaRanges.map((range, index) => ({
        value: featureData.counts[index],
        name: range
    }));

    const areaRangeChart = echarts.getInstanceByDom(document.getElementById('area-range-chart'));
    if (areaRangeChart && !areaRangeChart.isDisposed()) {
        areaRangeChart.setOption({
            series: [{
                data: pieData
            }]
        });
    }
}

// 更新小区信息滚动
function updateCommunityScroll() {
    if (communityData.length === 0) return;

    const container = document.getElementById('community-list');
    container.innerHTML = '';

    // 添加列表项 - 使用正确的字段名
    communityData.forEach(community => {
        const li = document.createElement('li');
        li.innerHTML = `
            <span class="community-name">${community.community}</span>
            <span class="community-district">${community.district}</span>
            <span class="community-price">${community.avgPricePerSqm}万</span>
            <span class="community-year">${community.avgBuildYear}</span>
        `;
        container.appendChild(li);
    });

    // 克隆列表项以实现无缝滚动
    const items = container.querySelectorAll('li');
    items.forEach(item => {
        const clone = item.cloneNode(true);
        container.appendChild(clone);
    });
}

// 页面加载完成后初始化仪表盘
document.addEventListener('DOMContentLoaded', initDashboard);