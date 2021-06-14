package response.parser.impl

import java.io.FileNotFoundException

import org.scalatest.FunSuite
import response.impl.FileEventsResourceParser

class FileEventsResourceParserTest extends FunSuite {

  private val resourceParser = new FileEventsResourceParser()

  test("given a valid source, should return a list of the expected rows") {
      val actualResult = resourceParser.parse("src/test/resources/sample1.txt")
      val expectedResult = Seq(
        "0x801002",
        "0xf81016",
        "0x1d8102f",
        "0x248202a",
        "0x2e0203e",
        "0x348204e",
        "0x3b8384b",
        "0x468385e",
        "0x5304059",
        "0x640406e",
        "0x6d8506a",
        "0x760606a",
        "0x838607e",
        "0x8e8707a",
        "0x930708e",
        "0x9f0709e",
        "0xad070a5",
        "0xb7880a2",
        "0xbf880b6",
        "0xc9080c6",
        "0xd2090c2",
        "0xdd090d6",
        "0xed0a8d3",
        "0xf98a8e6",
        "0x10a8b8e2",
        "0x1178b8ed",
        "0x1228c8ea",
        "0x12b0d8ea"
      )
    assert(actualResult == expectedResult)

    val actualResult2 = resourceParser.parse("src/test/resources/sample2.txt")
    val expectedResult2 = Seq(
      "0x781002",
      "0xe01016",
      "0x1081014",
      "0x1e0102f",
      "0x258202a",
      "0x308203e",
      "0x388204e",
      "0x388204e",
      "0x3d0384b",
      "0x478385e",
      "0x618406e",
      "0x5404059",
      "0x6b8506a",
      "0x750706c",
      "0x7d8507e",
      "0x938608e",
      "0x8b8607a",
      "0xa10609e",
      "0xb8870a2",
      "0xc4870b6",
      "0xcc070c6",
      "0x2ee74753",
      "0xd5080c2",
      "0xdf080d6",
      "0xe4098d3",
      "0xec098f6",
      "0xfc8a8e2",
      "0x10a8a8ed",
      "0x1180b8ea",
      "0x1218c8ea",
    )
      assert(actualResult2 == expectedResult2)
  }

  test("passing an invalid file location should throw FileNotFoundException") {
    assertThrows[FileNotFoundException] {
      resourceParser.parse("")
    }
  }

}
