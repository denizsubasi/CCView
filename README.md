<p align="center">
 <img align="center" src="https://raw.githubusercontent.com/denizsubasi/CreditCardView/master/art/ui_from_dribbble.gif"/>
</p>

<a href="https://dribbble.com/shots/6440077-Add-a-New-Credit-Card-alternate-flow" target="_blank"> Check this cool ui at Dribbble</a>

# CCView
CCView is a ready made credit card creation library.

# Features
-	Auto selection card type based on the credit card number pattern.
-	Auto selection of logo drawable based on the credit card type Visa, Mastercard, American Express, Discover Card
- Card number, cvv and expiry date validation.
- 4 different card formats.
- Edit actual credit card.

## Usage
```kotlin
startActivityForResult(AddCreditCardActivity.newIntent(context = this), REQUEST_CODE)
// If you want to edit selected credit card
startActivityForResult(AddCreditCardActivity.newIntent(context = this,creditCardItem = creditCardItem), REQUEST_CODE)
```

```kotlin
override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
   if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            creditCardItem = data?.extras?.getParcelable<CreditCardItem>(AddCreditCardActivity.KEY_CREDIT_CARD)
    }
}
```


# Dependency

```groovy
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
    implementation 'com.github.denizsubasi:CreditCardView:0.3'
}
```


License
--------


    Copyright 2020 Deniz Subaşı

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
