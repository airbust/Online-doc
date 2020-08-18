<template>
  <div style="height:100%;">
    <Header style="height:7%"/>
    <el-container style="height:93%">
      <el-aside  style="width:16%;">
        <Left/>
      </el-aside>
      <el-main style="width: 84%">
        <router-view/>
      </el-main>
    </el-container>
  </div>
</template>

<script>
import Header from "@/components/Header"; 
import Left from "@/components/Left";
export default {
  name: "Layout",
  components: {
    Header,Left
  },
}
</script>