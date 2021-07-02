package moe.kouyou.mcs

typealias TokenType = Int

class Token(val type: TokenType, val value: String)

object TokenTypes {
  val number: TokenType = 0x00
  val string: TokenType = 0x01
  val boolTrue: TokenType = 0x02
  val boolFalse: TokenType = 0x03

  val ident: TokenType = 0x04
  
  val opAssign: TokenType = 0x10

  val opAdd: TokenType = 0x11
  val opSub: TokenType = 0x12
  val opMul: TokenType = 0x13
  val opDiv: TokenType = 0x14
  val opMod: TokenType = 0x15

  val opEq: TokenType = 0x16 // ==
  val opNe: TokenType = 0x17 // !=
  val opGt: TokenType = 0x18 // >
  val opLt: TokenType = 0x19 // <
  val opGe: TokenType = 0x1A // >=
  val opLe: TokenType = 0x1B // <=

  val lpr: TokenType = 0x20 // L Parenthese (
  val rpr: TokenType = 0x21 // R Parenthese )
  val lbr: TokenType = 0x22 // L Brace {
  val rbr: TokenType = 0x23 // R Brace }
  val lbk: TokenType = 0x24 // L Bracket [
  val rbk: TokenType = 0x25 // R Bracket ]

  val dot: TokenType = 0x26
  val comma: TokenType = 0x27

  val eos: TokenType = 0x28  // end of statement ; \n \r

  val kwVar: TokenType = 0x30
  val kwIf: TokenType = 0x31
  val kwElse: TokenType = 0x32
  val kwElif: TokenType = 0x33
  val kwWhile: TokenType = 0x34
  val kwContinue: TokenType = 0x35
  val kwBreak: TokenType = 0x36
  val kwHalt: TokenType = 0x37
}
