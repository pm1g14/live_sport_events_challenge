package utils

import org.scalatest.{FunSuite, Ignore}

class DataConversionUtilsTest extends FunSuite {

  test("convertStringToHexInt is invoked with valid hex 0x781002 should return expected Int") {
    val actual = DataConversionUtils.convertStringToHexInt("0x781002")
    val expected = 7868418
    assert(actual == expected)
  }

  test("convertStringToHexInt is invoked with invalid hex should return with exception") {
    assertThrows[NumberFormatException] {
      DataConversionUtils.convertStringToHexInt("iaejfd")
    }
  }

  test("convertHexStringToBin is invoked with valid hex 0x781002 should return expected binary") {
    val actual = DataConversionUtils.convertHexStringToBin("0x781002")
    val expected = "00000000011110000001000000000010"
    assert(actual == expected)
  }

  test("convertHexStringToBin is invoked with empty string should throw NumberFormatException") {
    assertThrows[NumberFormatException] {
      DataConversionUtils.convertHexStringToBin("")
    }
  }

  test("convertHexStringToBin is invoked with big hex 9C72E0FA11C2E6A8 should throw NumberFormatException") {
    assertThrows[NumberFormatException] {
      DataConversionUtils.convertHexStringToBin("9C72E0FA11C2E6A8")
    }
  }

  test("reverseBinBits is invoked with valid 1001001 should return 1001001") {
    val actual1 = DataConversionUtils.reverseBinBits("1001001")
    val expected1 = "1001001"
    assert(actual1 == expected1)
    val actual2 = DataConversionUtils.reverseBinBits("10100")
    val expected2 = "00101"
    assert(actual2 == expected2)
  }

  test("binToInt is invoked with valid 100001001001 should return 2121") {
    val actual = DataConversionUtils.binToInt("100001001001")
    val expected = 2121
    assert(actual == expected)
  }

  test("binToInt is invoked with empty string should throw NumberFormatException") {
    assertThrows[NumberFormatException] {
      DataConversionUtils.binToInt("")
    }
  }


}
