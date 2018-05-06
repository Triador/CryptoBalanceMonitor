import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { AppComponent } from './app.component';
import { BalanceComponent } from './balance/balance.component';
import { AppRoutingModule } from './app.routing.module';
import {BalanceService} from './balance/balance.service';
import {HttpClientModule} from "@angular/common/http";

@NgModule({
  declarations: [
    AppComponent,
    BalanceComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule
  ],
  providers: [BalanceService],
  bootstrap: [AppComponent]
})
export class AppModule { }