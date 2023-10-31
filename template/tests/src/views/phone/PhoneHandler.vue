<template>
  <div>
    <div data-v-103fcbaa="" data-v-2f8ae276="" :class="!cardIsTrue?'-active':''" class="card-item " style="margin-top: 10px"
         @click="cardIsTrue = !cardIsTrue"
    >
      <div data-v-103fcbaa="" class="card-item__side -front">
        <div data-v-103fcbaa="" class="card-item__focus"/>
        <div data-v-103fcbaa="" aria-label="" class="card-item__cover">
          <img
            data-v-103fcbaa=""
            src="../../icons/img/130000.jpg"
            alt="Background image"
            class="card-item__bg"
          ></div>
        <div data-v-103fcbaa="" class="card-item__wrapper">
          <div data-v-103fcbaa="" class="card-item__top">
            <img
              data-v-103fcbaa=""
              src="../../icons/img/Chip.png"
              alt="Card chip image"
              class="card-item__chip"
            >
            <div data-v-103fcbaa="" class="card-item__type">
              <img
                data-v-103fcbaa=""
                src="data:image/png;base64,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"
                alt="mastercard brand image"
                class="card-item__typeImg"
              >
            </div>
          </div>
          <label data-v-103fcbaa="" aria-label="Card number" class="card-item__number">
              <span v-for=" (item_4,i) in info.card.split('')" :key="item_4.char" data-v-103fcbaa="">
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
                <span>{{info.cardHolder}}</span>
              </div>
            </label>

            <div data-v-103fcbaa="" class="card-item__date">
              <label data-v-103fcbaa="" aria-label="CVV" class="card-item__dateTitle">CVV</label>
              <label data-v-103fcbaa="" aria-label="CVV" class="card-item__dateItem">
                <span data-v-103fcbaa="">{{ info.cvv }}</span>
              </label>
            </div>
            <div data-v-103fcbaa="" class="card-item__date">
              <label data-v-103fcbaa="" aria-label="Expiration date" class="card-item__dateTitle">Expires</label>
              <label data-v-103fcbaa="" aria-label="Card month" class="card-item__dateItem">
                <span data-v-103fcbaa="">{{ info.cardDate.split('/')[0] }}</span>
              </label>
              /
              <label data-v-103fcbaa="" aria-label="Card year" class="card-item__dateItem">
                <span data-v-103fcbaa="">{{ info.cardDate.split('/')[1] }}</span>
              </label>
            </div>
          </div>
        </div>
      </div>

      <div data-v-103fcbaa="" class="card-item__side -back">
        <div data-v-103fcbaa="" aria-label="" class="card-item__cover">
          <img
            data-v-103fcbaa=""
            src="../../icons/img/60000.jpg"
            alt="Background image"
            class="card-item__bg"
          ></div>
        <div data-v-103fcbaa="" class="card-item__band"/>
        <div data-v-103fcbaa="" class="card-item__cvv">
          <label data-v-103fcbaa="" aria-label="Card CVV">
            <div data-v-103fcbaa="" class="card-item__cvvTitle">CVV</div>
            <div data-v-103fcbaa="" class="card-item__cvvBand">
              <span data-v-103fcbaa="">{{ info.cvv }}</span></div>
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
      <p data-v-2f8ae276="">
        <label data-v-2f8ae276="">姓名: </label>
        <span data-v-2f8ae276="">{{info.name}}</span>
      </p>
      <p data-v-2f8ae276="">
        <label data-v-2f8ae276="">手机号码: </label>
        <span data-v-2f8ae276="">{{ info.phone }}</span>
      </p>
      <p data-v-2f8ae276="">
        <label data-v-2f8ae276="">地址1: </label>
        <span data-v-2f8ae276="">{{info.address}}</span>
      </p>
      <p data-v-2f8ae276="">
        <label data-v-2f8ae276="">邮编: </label>
        <span data-v-2f8ae276="">{{ info.post }}</span>
      </p>
    </div>
  </div>
</template>

<script>
  import { getByUid } from '@/api/card'

  export default {
    name: 'PhoneHandler',
    data() {
      return {
        cardIsTrue: true,
        cardHolder: '',
        cardNumber: '',
        cardName: '',
        cardNameFirst: '',
        cardDate: '',
        cardCvv: '',
        info: {
          card:'',
          cardDate:''
        }
      }
    },
    mounted() {
      let uid = this.$route.query.uid
      this.getInfo(uid);
    },
    methods: {
      getInfo(uid) {
        getByUid({ uid: uid }).then(resp => {
            this.info=resp.data;
        })
      }

    }
  }
</script>

<style scoped>

</style>
