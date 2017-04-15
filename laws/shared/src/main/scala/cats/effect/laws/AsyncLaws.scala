/*
 * Copyright 2017 Daniel Spiewak
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cats
package effect
package laws

import cats.laws._

trait AsyncLaws[F[_]] extends MonadErrorLaws[F, Throwable] {
  implicit def F: Async[F]

  def asyncRightIsPure[A](a: A) =
    F.async[A](_(Right(a))) <-> F.pure(a)

  def asyncLeftIsRaiseError(t: Throwable) =
    F.async[Nothing](_(Left(t))) <-> F.raiseError(t)

  def thrownInRegisterIsRaiseError(t: Throwable) =
    F.async[Nothing](_ => throw t) <-> F.raiseError(t)

  // TODO law for once-only evaluation
}

object AsyncLaws {
  def apply[F[_]](implicit F0: Async[F]): AsyncLaws[F] = new AsyncLaws[F] {
    val F = F0
  }
}
