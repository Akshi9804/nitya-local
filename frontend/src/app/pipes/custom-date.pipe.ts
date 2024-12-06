import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'customDate',
  standalone: true
})
export class CustomDatePipe implements PipeTransform {

  transform(value: Date | string | number): string {
    if (!value) return '';

    const utcDate = new Date(value);

    // Convert to IST (UTC+5:30)
    const offset = 5.5 * 60 * 60 * 1000; // 5.5 hours in milliseconds
    const istDate = new Date(utcDate.getTime() + offset);

    const hours = istDate.getHours().toString().padStart(2, '0');
    const minutes = istDate.getMinutes().toString().padStart(2, '0');
    const day = istDate.getDate().toString().padStart(2, '0');
    const month = (istDate.getMonth() + 1).toString().padStart(2, '0');
    const year = istDate.getFullYear();

    return `Time: ${hours}:${minutes}  Date: ${day}-${month}-${year}`;
  }
}
