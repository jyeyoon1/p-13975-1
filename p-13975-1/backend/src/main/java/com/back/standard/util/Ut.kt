package com.back.standard.util

import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.Jwts.claims
import io.jsonwebtoken.security.Keys
import lombok.SneakyThrows
import org.graalvm.compiler.core.common.type.StampFactory.`object`
import java.io.BufferedReader
import java.io.InputStreamReader
import java.security.Key
import java.util.*

object Ut {
    object jwt {
        @JvmStatic
        fun toString(secret: String, expireSeconds: Int, body: Map<String, Any>): String {

            val issuedAt = Date()
            val expiration = Date(issuedAt.time + 1000L * expireSeconds)

            val secretKey: Key = Keys.hmacShaKeyFor(secret.toByteArray())

            val jwt = Jwts.builder()
                .claims(body)
                .issuedAt(issuedAt)
                .expiration(expiration)
                .signWith(secretKey)
                .compact()

            return jwt
        }

        @JvmStatic
        fun isValid(secret: String, jwtStr: String): Boolean {
            return try {
                val secretKey = Keys.hmacShaKeyFor(secret.toByteArray())

                Jwts
                    .parser()
                    .verifyWith(secretKey)
                    .build()
                    .parse(jwtStr)

                true
            } catch (e: Exception) {
                false
            }
        }

        @JvmStatic
        fun payload(secret: String, jwtStr: String): Map<String, Any>? {
            return try{
                val secretKey = Keys.hmacShaKeyFor(secret.toByteArray())

                Jwts
                    .parser()
                    .verifyWith(secretKey)
                    .build()
                    .parse(jwtStr)
                    .payload as Map<String, Any>
            } catch (e: Exception) {
                null
            }
        }
    }

    object json {
        lateinit var objectMapper: ObjectMapper

        @JvmStatic
        @JvmOverloads
        fun toString(obj: Any, defaultValue: String = ""): String {
            return try {
                objectMapper.writeValueAsString(obj)
            } catch (e: Exception) {
                defaultValue
            }
        }
    }

    object cmd {
        fun run(vararg args: String) {
            val isWindows = System
                .getProperty("os.name")
                .lowercase(Locale.getDefault())
                .contains("win")

            val builder = ProcessBuilder(
                args
                    .map { it.replace("{{DOT_CMD}}",  if (isWindows) ".cmd" else "") }
                    .toList()
            )

            // 에러 스트림도 출력 스트림과 함께 병합
            builder.redirectErrorStream(true)

            // 프로세스 시작
            val process = builder.start()

            BufferedReader(InputStreamReader(process.getInputStream())).use { reader ->
                var line: String?
                while ((reader.readLine().also { line = it }) != null) {
                    println(line) // 결과 한 줄씩 출력
                }
            }
            process.inputStream.bufferedReader().useLines { lines ->
                lines.forEach { println(it) }
            }
            // 종료 코드 확인
            val exitCode = process.waitFor()
            println("종료 코드: $exitCode")
        }

        @JvmStatic
        fun runAsync(vararg args: String) {
            Thread(Runnable {
                run(*args)
            }).start()
        }
    }
}
