# Spark
> Apache Spark™ is a fast and general engine for large-scale data processing.

* version 1.6.2

## 개요
* 스파크는 개발자가 비지니스 로직을 함수로 정의한 후에, 이 함수를 스파크 클러스의 노드들로 보내서 수행
* 스파크 내에 저장된 데이타를 RDD라고 하고, 변경이 불가능
* RDD 는 여러 분산 노드에 걸쳐서 저장되는 변경이 불가능한 데이타(객체)의 집합
* 각각의 RDD는 여러개의 파티션으로 분리가 된다. (서로 다른 노드에서 분리되서 실행되는)

## Apache Spark Cluster 구조 
* 마스터/슬레이브 구조
  * 중앙 조정자(coordinator) + 분산 작업 노드
  * 드라이버 + 익스큐터(executor) ==> 스파크 애플리케이션
* Driver Program(SparkContext) - Cluster Manager - Worker Node (Executor:Task)
* Executor: Process
* Task: A Unit of work that will sent to one executor

## 스파크 애플리케이션
* 클러스터에서 병렬 연산을 수행하는 드라이버 프로그램
  - main() 함수로 실행
  - SparkContext를 생성
  - 분산 데이터세트(RDD) 정의
  - 트랜스포메이션과 액션(연산작업...)을 실행
  - 드라이버가 종료되면 애플리케이션도 종료

## RDD 

* 분산되어 존재하는 데이터 요소들의 모임
* RDD 생성, RDD 변형, 계산 결과를 위해서 RDD에서 연산을 호출
* 연산
  * 트랜스포메이션(transformation)
  * 액션(action)

## 분산 클러스터 설정

### Nodes of the cluster

* Cluster Metrics

| Memory Total | VCores Total | Active Nodes
|:-------|:------------|:-------------|:------------
|683.59G | 100 | 5

* Scheduler Metrics   

| Scheduler Type | Scheduling Resource Type | Minimum Allocation | Maximum Allocation 
|:---------------|:-------------------------|:-------------------|:------------------
| Fair Scheduler | [MEMORY,CPU] | <memory:4096, vCores:1> | <memory:140000, vCores:20> 

* Node Metrics   

| Node Labels | Node Address | Mem Avail | VCores Avail 
|:------------|:-------------|:----------|:------------
| coordinator | dev-01 | <memory:4096, vCores:1> | <memory:140000, vCores:20> 


### 자원 사용량 설정
> 스파크 애플리케이션이 사용할 자원의 한계 설정

   * 얀에서는 작업 큐로 할당
   * 익스큐터 개수
   * 익스큐터 메모리
   * 코어 개수

Note: 대개 고용량 설정의 익스큐터를 적은 개수로 실행하는 것이 더 나은 성능을 보임

### Fair Scheduler

* Modifying configuration at runtime  
: It is possible to modify minimum shares, limits, weights, preemption timeouts and queue scheduling policies at runtime by editing the allocation file. The scheduler will reload this file 10-15 seconds after it sees that it was modified.

* Moving applications between queues

```
$ yarn application -movetoqueue appID -queue targetQueueName
```

참고>  
https://hadoop.apache.org/docs/r2.7.3/hadoop-yarn/hadoop-yarn-site/FairScheduler.html