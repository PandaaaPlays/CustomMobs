import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject } from 'rxjs';

export interface Option {
  optionName: string,
  key: string,
  description: string,
  type: string | null,
  minimum: number | null,
  maximum: number | null
}

@Injectable({
  providedIn: 'root'
})
export class OptionsService {
  constructor(private http: HttpClient) {}

  private optionsSubject = new BehaviorSubject<{ [key: string]: Option[] }>({});
  options$ = this.optionsSubject.asObservable();

  fetchOptions(): void {
    this.http.get<{ [key: string]: Option[] }>('assets/data/options.json')
      .subscribe(data => {
        const sortedEntries = Object.entries(data)
          .sort(([keyA], [keyB]) => {
            if (keyA === 'Default') return -1;
            if (keyB === 'Default') return 1;
            return keyA.localeCompare(keyB);
          });

        const sortedData: { [key: string]: Option[] } = Object.fromEntries(sortedEntries);

        this.optionsSubject.next(sortedData);
      });
  }

}
