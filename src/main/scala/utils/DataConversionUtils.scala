package utils

import scala.util.Try

object DataConversionUtils {

  private val hexPrefix = "0x"
  private val hexRadix = 16
  private val binaryRadix = 2
  private val binaryLength = 32

  def convertStringToHexInt(hex_string:String): Int = {
    val hexStringConverted = hex_string.replace(hexPrefix, "")
    Try(Integer.parseInt(hexStringConverted, hexRadix)).toOption match {
      case Some(value) => value
      case _           => throw new NumberFormatException(s"Given $hex_string is not a valid hex representation")
    }
  }

  def convertHexStringToBin(hex_string:String): String = {
    val hexConverted: Int = convertStringToHexInt(hex_string)
    convertToBinary(hexConverted)
  }


  def reverseBinBits(bin: String): String = bin.toCharArray.reverse.mkString


  def binToInt(binary: String): Int = Try(Integer.parseInt(binary, binaryRadix)).toOption match {
    case Some(value) => value
    case _           => throw new NumberFormatException(s"Given string $binary is not in binary format")
  }


  private def convertToBinary(i: Int): String =
    String.format("%" + binaryLength + "s", Integer.toBinaryString(i)).replaceAll(" ", "0")

}
